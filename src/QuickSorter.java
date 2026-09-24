
import java.util.SplittableRandom;

public final class QuickSorter {
    private final SplittableRandom random;
    private AlgorithmMetrics metrics = new AlgorithmMetrics();

    public QuickSorter() { this(System.nanoTime()); }
    public QuickSorter(long seed) { this.random = new SplittableRandom(seed); }

    public void sort(int[] a) {
        if (a == null) throw new IllegalArgumentException("array must not be null");
        metrics = new AlgorithmMetrics();
        if (a.length < 2) return;
        quickSort(a, 0, a.length - 1, 1);
    }

    public AlgorithmMetrics metrics() { return metrics.copy(); }

    private void quickSort(int[] a, int lo, int hi, int depth) {
        metrics.recursiveCall();
        metrics.depth(depth);

        while (lo < hi) {
            int pivotValue = a[random.nextInt(lo, hi + 1)];
            int[] equalRange = partition3(a, lo, hi, pivotValue);
            int lt = equalRange[0];
            int gt = equalRange[1];

            int leftSize = lt - lo;
            int rightSize = hi - gt;

            if (leftSize < rightSize) {
                if (lo < lt - 1) quickSort(a, lo, lt - 1, depth + 1);
                lo = gt + 1;
            } else {
                if (gt + 1 < hi) quickSort(a, gt + 1, hi, depth + 1);
                hi = lt - 1;
            }
        }
    }

    private int[] partition3(int[] a, int lo, int hi, int pivot) {
        int lt = lo;
        int i = lo;
        int gt = hi;
        while (i <= gt) {
            metrics.comparison();
            if (a[i] < pivot) {
                swap(a, lt++, i++);
            } else {
                metrics.comparison();
                if (a[i] > pivot) swap(a, i, gt--);
                else i++;
            }
        }
        return new int[]{lt, gt};
    }

    private void swap(int[] a, int i, int j) {
        if (i == j) return;
        int t = a[i]; a[i] = a[j]; a[j] = t;
        metrics.swap();
    }
}
