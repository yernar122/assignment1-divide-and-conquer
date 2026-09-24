public final class DeterministicSelector {
    private AlgorithmMetrics metrics = new AlgorithmMetrics();

    public int select(int[] a, int k) {
        if (a == null) throw new IllegalArgumentException("array must not be null");
        if (a.length == 0) throw new IllegalArgumentException("array must not be empty");
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("k out of range");
        metrics = new AlgorithmMetrics();
        return select(a, 0, a.length - 1, k, 1);
    }

    public AlgorithmMetrics metrics() { return metrics.copy(); }

    private int select(int[] a, int lo, int hi, int k, int depth) {
        metrics.recursiveCall();
        metrics.depth(depth);

        while (true) {
            if (lo == hi) return a[lo];
            if (hi - lo + 1 <= 5) {
                insertionSort(a, lo, hi);
                return a[k];
            }

            int pivot = medianOfMedians(a, lo, hi, depth + 1);
            int[] range = partition3(a, lo, hi, pivot);
            if (k < range[0]) {
                hi = range[0] - 1;
            } else if (k > range[1]) {
                lo = range[1] + 1;
            } else {
                return a[k];
            }
        }
    }

    private int medianOfMedians(int[] a, int lo, int hi, int depth) {
        int write = lo;
        for (int start = lo; start <= hi; start += 5) {
            int end = Math.min(start + 4, hi);
            insertionSort(a, start, end);
            int medianIndex = start + (end - start) / 2;
            swap(a, write++, medianIndex);
        }
        int count = write - lo;
        int medianK = lo + count / 2;
        return select(a, lo, write - 1, medianK, depth);
    }

    private int[] partition3(int[] a, int lo, int hi, int pivot) {
        int lt = lo, i = lo, gt = hi;
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

    private void swap(int[] a, int i, int j) {
        if (i == j) return;
        int t = a[i]; a[i] = a[j]; a[j] = t;
        metrics.swap();
    }
}
