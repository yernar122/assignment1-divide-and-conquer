package kz.aitu.daa;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClosestPairSolverTest {
    @Test
    void matchesBruteForceForSmallDatasets() {
        Random rnd = new Random(20260914L);
        for (int test = 0; test < 40; test++) {
            int n = 2 + rnd.nextInt(300); // below the required n <= 2,000 verification limit
            List<Point> points = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                points.add(new Point(rnd.nextDouble() * 10_000, rnd.nextDouble() * 10_000));
            }
            double expected = ClosestPairSolver.bruteForceDistance(points);
            double actual = new ClosestPairSolver().solve(points).distance();
            assertEquals(expected, actual, 1e-9, "failed random dataset " + test);
        }
    }

    @Test
    void handlesDuplicatePoints() {
        List<Point> points = List.of(new Point(1, 1), new Point(5, 7), new Point(1, 1), new Point(9, 2));
        assertEquals(0.0, new ClosestPairSolver().solve(points).distance(), 0.0);
    }
}
