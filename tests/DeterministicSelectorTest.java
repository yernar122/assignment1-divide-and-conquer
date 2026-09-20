package kz.aitu.daa;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeterministicSelectorTest {
    @Test
    void matchesSortedReferenceForAtLeastOneHundredRandomTests() {
        Random rnd = new Random(20260914L);
        for (int test = 0; test < 200; test++) {
            int n = 1 + rnd.nextInt(500);
            int[] input = rnd.ints(n, -100, 101).toArray(); // duplicates are intentional
            int k = rnd.nextInt(n);

            int[] expected = input.clone();
            Arrays.sort(expected);

            int actual = new DeterministicSelector().select(input.clone(), k);
            assertEquals(expected[k], actual, "failed at random test " + test + ", k=" + k);
        }
    }
}
