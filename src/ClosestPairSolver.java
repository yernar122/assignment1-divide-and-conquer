package kz.aitu.daa;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** O(n log n) closest-pair divide-and-conquer implementation. */
public final class ClosestPairSolver {
    public record Result(Point first, Point second, double distance, AlgorithmMetrics metrics) { }
    private record IndexedPoint(int id, Point point) { }
    private record InternalResult(IndexedPoint first, IndexedPoint second, double distance) { }

    private AlgorithmMetrics metrics = new AlgorithmMetrics();

    public Result solve(List<Point> points) {
        if (points == null) throw new IllegalArgumentException("points must not be null");
        if (points.size() < 2) throw new IllegalArgumentException("at least two points are required");
        metrics = new AlgorithmMetrics();

        List<IndexedPoint> byX = new ArrayList<>(points.size());
        for (int i = 0; i < points.size(); i++) byX.add(new IndexedPoint(i, points.get(i)));
        byX.sort(Comparator.comparingDouble((IndexedPoint p) -> p.point().x())
                .thenComparingDouble(p -> p.point().y())
                .thenComparingInt(IndexedPoint::id));

        List<IndexedPoint> byY = new ArrayList<>(byX);
        byY.sort(Comparator.comparingDouble((IndexedPoint p) -> p.point().y())
                .thenComparingDouble(p -> p.point().x())
                .thenComparingInt(IndexedPoint::id));

        InternalResult r = solveRecursive(byX, byY, 1);
        return new Result(r.first().point(), r.second().point(), r.distance(), metrics.copy());
    }

    public static double bruteForceDistance(List<Point> points) {
        if (points == null || points.size() < 2) throw new IllegalArgumentException("at least two points are required");
        double best = Double.POSITIVE_INFINITY;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                best = Math.min(best, distance(points.get(i), points.get(j)));
            }
        }
        return best;
    }

    private InternalResult solveRecursive(List<IndexedPoint> byX, List<IndexedPoint> byY, int depth) {
        metrics.recursiveCall();
        metrics.depth(depth);
        int n = byX.size();
        if (n <= 3) return bruteForceIndexed(byX);

        int mid = n / 2;
        List<IndexedPoint> leftX = new ArrayList<>(byX.subList(0, mid));
        List<IndexedPoint> rightX = new ArrayList<>(byX.subList(mid, n));
        double midX = byX.get(mid).point().x();

        Set<Integer> leftIds = new HashSet<>(mid * 2);
        for (IndexedPoint p : leftX) leftIds.add(p.id());
        List<IndexedPoint> leftY = new ArrayList<>(mid);
        List<IndexedPoint> rightY = new ArrayList<>(n - mid);
        for (IndexedPoint p : byY) {
            if (leftIds.contains(p.id())) leftY.add(p);
            else rightY.add(p);
        }

        InternalResult left = solveRecursive(leftX, leftY, depth + 1);
        InternalResult right = solveRecursive(rightX, rightY, depth + 1);
        InternalResult best = left.distance() <= right.distance() ? left : right;
        double delta = best.distance();

        List<IndexedPoint> strip = new ArrayList<>();
        for (IndexedPoint p : byY) {
            metrics.comparison();
            if (Math.abs(p.point().x() - midX) < delta) strip.add(p);
        }

        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size(); j++) {
                metrics.comparison();
                if (strip.get(j).point().y() - strip.get(i).point().y() >= delta) break;
                double d = measuredDistance(strip.get(i).point(), strip.get(j).point());
                if (d < delta) {
                    delta = d;
                    best = new InternalResult(strip.get(i), strip.get(j), d);
                }
            }
        }
        return best;
    }

    private InternalResult bruteForceIndexed(List<IndexedPoint> points) {
        InternalResult best = null;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                double d = measuredDistance(points.get(i).point(), points.get(j).point());
                if (best == null || d < best.distance()) {
                    best = new InternalResult(points.get(i), points.get(j), d);
                }
            }
        }
        if (best == null) throw new IllegalStateException("recursive subset must contain at least two points");
        return best;
    }

    private double measuredDistance(Point a, Point b) {
        metrics.comparison(); // distance evaluation is the algorithm-specific extra operation metric
        return distance(a, b);
    }

    private static double distance(Point a, Point b) {
        return Math.hypot(a.x() - b.x(), a.y() - b.y());
    }
}
