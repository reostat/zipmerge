package org.zip;

import java.util.*;
import java.util.stream.Collector;

/**
 * Implementation of ranges merge algorithm based on sorted list and stack.
 * <p/>
 * Implementation sorts input collection by range min first then iterates over it
 * using a stack to collect and merge ranges.
 * <p/>
 * Algorithm complexity is O(n*log n) due to sorting.
 * Memory complexity is O(n) since it stores a copy of resulting collection in a stack
 * <p/>
 * Implementation is thread-safe; {@link ZipRangeListMerger#merge(Collection)} can be called
 * from multiple threads.
 */
class ZipRangeListMerger {

    /**
     * Main algorithm is implemented as {@link java.util.stream.Collector} with {@link ZipRange} as
     * input type, <code>LinkedList&lt;ZipRange&gt;</code> as accumulator type and <code>List&lt;ZipRange&gt;</code>
     * as return type
     */
    private static final Collector<ZipRange, LinkedList<ZipRange>, List<ZipRange>> MERGING_COLLECTOR =
            Collector.of(LinkedList::new, ZipRangeListMerger::accumulate,
                    ZipRangeListMerger::combine, ZipRangeListMerger::finish);

    /**
     * Comparator for ZipRange.min field (ascending).
     */
    private static final Comparator<ZipRange> COMPARE_BY_MIN_ASC = Comparator.comparingInt(ZipRange::getMin);

    /**
     * Main method; see {@link ZipRangeMerger#merge(Collection)}
     */
    static List<ZipRange> merge(Collection<ZipRange> ranges) {
        // sort ranges first then collect them with merge
        return ranges.stream().sorted(COMPARE_BY_MIN_ASC).collect(MERGING_COLLECTOR);
    }

    private static void accumulate(LinkedList<ZipRange> acc, ZipRange elem) {
        // acc is used as a stack
        if (overlap(acc.peek(), elem)) {
            // if range on top of the stack overlaps with next range in collection
            // then pop it, merge with the next range and push the result back
            acc.push(merge(acc.pop(), elem));
        } else  {
            // otherwise just push the next range to stack
            acc.push(elem);
        }
    }

    private static List<ZipRange> finish(LinkedList<ZipRange> result) {
        // stack is going to have the results in reverse order
        Collections.reverse(result);
        return result;
    }

    private static LinkedList<ZipRange> combine(LinkedList<ZipRange> left, LinkedList<ZipRange> right) {
        // since this algorithm requires input collection to be presorted, it's pretty hard
        // to implement it's parallel version. not impossible, just complex to make it efficient
        throw new UnsupportedOperationException("parallel operations are not supported");
    }

    private static boolean overlap(ZipRange left, ZipRange right) {
        return left != null && right.getMin() <= left.getMax();
    }

    private static ZipRange merge(ZipRange left, ZipRange right) {
        return new ZipRange(left.getMin(), Math.max(left.getMax(), right.getMax()));
    }
}
