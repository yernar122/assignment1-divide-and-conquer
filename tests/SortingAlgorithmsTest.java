import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SortingAlgorithmsTest {
    @Test
    void mergeSortMatchesArraysSortOnRequiredCases() {
        for (int[] input : cases()) {
            int[] expected = input.clone();
            Arrays.sort(expected);
            int[] actual = input.clone();
            new MergeSorter().sort(actual);
            assertArrayEquals(expected, actual);
        }
    }

    @Test
    void quickSortMatchesArraysSortOnRequiredCases() {
        long seed = 100;
        for (int[] input : cases()) {
            int[] expected = input.clone();
            Arrays.sort(expected);
            int[] actual = input.clone();
            new QuickSorter(seed++).sort(actual);
            assertArrayEquals(expected, actual);
        }
    }

    @Test
    void quickSortSmallerFirstRecursionKeepsStackDepthLogarithmic() {
        int n = 100_000;
        int[] input = new Random(42).ints(n, -1_000_000, 1_000_001).toArray();
        QuickSorter sorter = new QuickSorter(42);
        sorter.sort(input);
        assertTrue(sorter.metrics().maxRecursionDepth() <= 20,
                "smaller-first recursion should keep stack depth O(log n)");
    }

    private static int[][] cases() {
        Random rnd = new Random(42);
        int[] random = rnd.ints(2_000, -10_000, 10_001).toArray();
        int[] sorted = new int[2_000];
        int[] reverse = new int[2_000];
        int[] duplicates = new int[2_000];
        for (int i = 0; i < 2_000; i++) {
            sorted[i] = i;
            reverse[i] = 2_000 - i;
            duplicates[i] = i % 7;
        }
        return new int[][]{random, sorted, reverse, duplicates, {}, {7}};
    }
}
