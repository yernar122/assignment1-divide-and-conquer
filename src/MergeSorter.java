package kz.aitu.daa;

public final class MergeSorter {
    private static final int INSERTION_SORT_CUTOFF = 24;
    private AlgorithmMetrics metrics = new AlgorithmMetrics();

    public void sort(int[] a) {
        if (a == null) throw new IllegalArgumentException("array must not be null");
        metrics = new AlgorithmMetrics();
        if (a.length < 2) return;
        int[] buffer = new int[a.length];
        mergeSort(a, buffer, 0, a.length - 1, 1);
    }

    public AlgorithmMetrics metrics() { return metrics.copy(); }

    private void mergeSort(int[] a, int[] buffer, int lo, int hi, int depth) {
        metrics.recursiveCall();
        metrics.depth(depth);
        if (hi - lo + 1 <= INSERTION_SORT_CUTOFF) {
            insertionSort(a, lo, hi);
            return;
        }

        int mid = lo + (hi - lo) / 2;
        mergeSort(a, buffer, lo, mid, depth + 1);
        mergeSort(a, buffer, mid + 1, hi, depth + 1);

        metrics.comparison();
        if (a[mid] <= a[mid + 1]) return;
        merge(a, buffer, lo, mid, hi);
    }

    private void merge(int[] a, int[] buffer, int lo, int mid, int hi) {
        System.arraycopy(a, lo, buffer, lo, hi - lo + 1);
        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = buffer[j++];
            } else if (j > hi) {
                a[k] = buffer[i++];
            } else {
                metrics.comparison();
                if (buffer[i] <= buffer[j]) a[k] = buffer[i++];
                else a[k] = buffer[j++];
            }
        }
    }

    private void insertionSort(int[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            int value = a[i];
            int j = i - 1;
            while (j >= lo) {
                metrics.comparison();
                if (a[j] <= value) break;
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = value;
        }
    }
}
