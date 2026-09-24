
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public final class Main {
    private Main() { }

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("--experiments")) {
            new Experiment().runAll(Path.of("results", "results.csv"));
            System.out.println("Experiments completed: results/results.csv");
            return;
        }

        int[] source = {9, 3, 5, 1, 8, 7, 2, 5, 5};

        int[] mergeData = source.clone();
        MergeSorter merge = new MergeSorter();
        merge.sort(mergeData);
        System.out.println("MergeSort: " + Arrays.toString(mergeData));
        System.out.println("  " + merge.metrics());

        int[] quickData = source.clone();
        QuickSorter quick = new QuickSorter(42);
        quick.sort(quickData);
        System.out.println("QuickSort: " + Arrays.toString(quickData));
        System.out.println("  " + quick.metrics());

        int[] selectData = source.clone();
        DeterministicSelector selector = new DeterministicSelector();
        int k = 4;
        int kth = selector.select(selectData, k);
        System.out.println("DeterministicSelect k=" + k + ": " + kth);
        System.out.println("  " + selector.metrics());

        List<Point> points = List.of(
                new Point(2, 3), new Point(12, 30), new Point(40, 50),
                new Point(5, 1), new Point(12, 10), new Point(3, 4));
        ClosestPairSolver solver = new ClosestPairSolver();
        ClosestPairSolver.Result closest = solver.solve(points);
        System.out.printf("ClosestPair: %s <-> %s, distance=%.6f%n",
                closest.first(), closest.second(), closest.distance());
        System.out.println("  " + closest.metrics());
    }
}
