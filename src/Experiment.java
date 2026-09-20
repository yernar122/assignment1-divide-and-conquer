package kz.aitu.daa;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public final class Experiment {

    private static final long SEED = 20260914L;
    private final Random random = new Random(SEED);

    public void runAll(Path csvPath) throws IOException {
        Files.createDirectories(csvPath.getParent());

        try (BufferedWriter out = Files.newBufferedWriter(csvPath)) {

            out.write(
                    "algorithm,n,input_type,time_ns,max_recursion_depth," +
                            "comparisons,swaps,recursive_calls,result\n"
            );

            runSortExperiments(out);
            runSelectExperiments(out);
            runClosestPairExperiments(out);
        }
    }

    private void runSortExperiments(BufferedWriter out) throws IOException {

        int[] sizes = {1_000, 5_000, 20_000, 50_000};

        String[] types = {"random", "sorted", "reverse", "duplicate-heavy"
        };

        for (int n : sizes) {
            for (String type : types) {

                int[] base = generateArray(n, type);

                // MergeSort
                int[] mergeArray = base.clone();
                MergeSorter mergeSorter = new MergeSorter();

                long start = System.nanoTime();
                mergeSorter.sort(mergeArray);
                long mergeTime = System.nanoTime() - start;

                ensureSorted(mergeArray);

                write(
                        out, "MergeSort", n, type, mergeTime, mergeSorter.metrics(), "sorted"
                );


                // QuickSort
                int[] quickArray = base.clone();
                QuickSorter quickSorter =
                        new QuickSorter(SEED + n + type.hashCode());

                start = System.nanoTime();
                quickSorter.sort(quickArray);
                long quickTime = System.nanoTime() - start;

                ensureSorted(quickArray);

                write( out, "QuickSort", n, type, quickTime, quickSorter.metrics(), "sorted"
                );
            }
        }
    }

    private void runSelectExperiments(BufferedWriter out) throws IOException {

        int[] sizes = {1_000, 5_000, 20_000, 50_000};

        for (int n : sizes) {

            int[] array = generateArray(n, "random");
            int k = n / 2;

            DeterministicSelector selector =
                    new DeterministicSelector();

            long start = System.nanoTime();
            int value = selector.select(array, k);
            long time = System.nanoTime() - start;

            write( out,"DeterministicSelect", n, "random", time, selector.metrics(), Integer.toString(value)
            );
        }
    }

    private void runClosestPairExperiments(BufferedWriter out)
            throws IOException {

        int[] sizes = {1_000, 5_000, 20_000, 50_000};

        for (int n : sizes) {

            List<Point> points = generatePoints(n);

            ClosestPairSolver solver =
                    new ClosestPairSolver();

            long start = System.nanoTime();
            ClosestPairSolver.Result result =
                    solver.solve(points);
            long time = System.nanoTime() - start;

            write( out, "ClosestPair", n, "random-points", time, result.metrics(),
                    String.format(
                            Locale.US,
                            "%.8f",
                            result.distance()
                    )
            );
        }
    }

    private int[] generateArray(int n, String type) {

        int[] array = new int[n];

        switch (type) {

            case "random" -> {
                for (int i = 0; i < n; i++) {
                    array[i] =
                            random.nextInt(n * 4 + 1) - n * 2;
                }
            }

            case "sorted" -> {
                for (int i = 0; i < n; i++) {
                    array[i] = i;
                }
            }

            case "reverse" -> {
                for (int i = 0; i < n; i++) {
                    array[i] = n - i;
                }
            }

            case "duplicate-heavy" -> {
                for (int i = 0; i < n; i++) {
                    array[i] = random.nextInt(10);
                }
            }

            default ->
                    throw new IllegalArgumentException(
                            "Unknown input type: " + type
                    );
        }

        return array;
    }

    private List<Point> generatePoints(int n) {

        List<Point> points = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {

            double x = random.nextDouble() * 100_000;
            double y = random.nextDouble() * 100_000;

            points.add(new Point(x, y));
        }

        return points;
    }

    private static void ensureSorted(int[] array) {

        for (int i = 1; i < array.length; i++) {

            if (array[i - 1] > array[i]) {
                throw new IllegalStateException(
                        "Array is not sorted"
                );
            }
        }
    }

    private static void write( BufferedWriter out, String algorithm, int n, String type,
            long time, AlgorithmMetrics metrics, String result
    ) throws IOException {

        out.write(
                algorithm + "," +
                        n + "," +
                        type + "," +
                        time + "," +
                        metrics.maxRecursionDepth() + "," +
                        metrics.comparisons() + "," +
                        metrics.swaps() + "," +
                        metrics.recursiveCalls() + "," +
                        result
        );

        out.newLine();
    }
}