public final class AlgorithmMetrics {
    private long comparisons;
    private long swaps;
    private long recursiveCalls;
    private int maxRecursionDepth;

    public void comparison() { comparisons++; }
    public void swap() { swaps++; }
    public void recursiveCall() { recursiveCalls++; }
    public void depth(int depth) { maxRecursionDepth = Math.max(maxRecursionDepth, depth); }

    public long comparisons() { return comparisons; }
    public long swaps() { return swaps; }
    public long recursiveCalls() { return recursiveCalls; }
    public int maxRecursionDepth() { return maxRecursionDepth; }

    public AlgorithmMetrics copy() {
        AlgorithmMetrics c = new AlgorithmMetrics();
        c.comparisons = comparisons;
        c.swaps = swaps;
        c.recursiveCalls = recursiveCalls;
        c.maxRecursionDepth = maxRecursionDepth;
        return c;
    }

    @Override
    public String toString() {
        return "comparisons=" + comparisons +
                ", swaps=" + swaps +
                ", recursiveCalls=" + recursiveCalls +
                ", maxDepth=" + maxRecursionDepth;
    }
}
