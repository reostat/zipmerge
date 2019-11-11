package org.zip;

import java.util.*;
import java.util.stream.Collector;

/**
 * Implementation of range merge algorithm based on {@link java.util.TreeSet}
 * <p/>
 * Implementation puts input ranges into TreeSet with custom comparator; collisions
 * are merged.
 * <p/>
 * Algorithm complexity is O(n) since it doesn't require pre-sorting.
 * Memory complexity is O(n) since it stores a copy of resulting collection in TreeSet.
 * <p/>
 * Implementation is thread-safe.
 */
class ZipRangeTreeMerger {

    /**
     * Main comparator; ranges are deemed equal if they overlap
     */
    private static final Comparator<ZipRange> MAIN_COMPARATOR = ZipRangeTreeMerger::overlapAsEqual;

    /**
     * Supplement comparator; of two overlapping ranges, the one with smaller lower bound is considered lesser
     */
    private static final Comparator<ZipRange> PUSH_LEFT_COMPARATOR =
            MAIN_COMPARATOR.thenComparing(ZipRangeTreeMerger::resolveEqual);

    /**
     * Main algorithm is implemented as {@link java.util.stream.Collector} with {@link ZipRange} as
     * input, <code>TreeSet&lt;ZipRange&gt;</code> as accumulator and <code>Collection&lt;ZipRange&gt;</code>
     * as return
     */
    private static final Collector<ZipRange, TreeSet<ZipRange>, Collection<ZipRange>> MERGING_COLLECTOR =
            Collector.of(() -> new TreeSet<>(PUSH_LEFT_COMPARATOR),
                    ZipRangeTreeMerger::accumulate,
                    ZipRangeTreeMerger::combine, ZipRangeTreeMerger::finish,
                    Collector.Characteristics.CONCURRENT);

    /**
     * Sequential version of {@link ZipRangeMerger#merge(Collection)}
     */
    static Collection<ZipRange> treeMerge(Collection<ZipRange> ranges) {
        return ranges.stream().collect(MERGING_COLLECTOR);
    }

    /**
     * Parallel version of {@link ZipRangeMerger#merge(Collection)}
     */
    static Collection<ZipRange> parallelTreeMerge(Collection<ZipRange> pairs) {
        return pairs.parallelStream().collect(MERGING_COLLECTOR);
    }

    private static void accumulate(TreeSet<ZipRange> acc, ZipRange elem) {
        // among already merged elements, find those that are equal
        // or greater to the new one
        Iterator<ZipRange> mergeCandidates = acc.tailSet(elem).iterator();
        while (mergeCandidates.hasNext()) {
            ZipRange candidate = mergeCandidates.next();
            if (overlap(elem, candidate)) {
                // if current range overlaps with the new one, replace current with merge result
                elem = merge(elem, candidate);
                mergeCandidates.remove();
            } else {
                break;
            }
        }
        // put new or merged element back into set
        acc.add(elem);
    }

    private static Collection<ZipRange> finish(TreeSet<ZipRange> result) {
        return result;
    }

    private static TreeSet<ZipRange> combine(TreeSet<ZipRange> left, TreeSet<ZipRange> right) {
        // simply merge the smaller set into the larger
        if (left.size() > right.size()) {
            right.forEach(elem -> accumulate(left, elem));
            return left;
        }
        left.forEach(elem -> accumulate(right, elem));
        return right;
    }

    private static int overlapAsEqual(ZipRange a, ZipRange b) {
        if (a.getMax() < b.getMin())
            return -1;
        if (a.getMin() > b.getMax())
            return 1;
        return 0;
    }

    private static int resolveEqual(ZipRange a, ZipRange b) {
        return a.getMin() < b.getMin() ? -1 : 0;
    }


    private static boolean overlap(ZipRange a, ZipRange b) {
        return overlapAsEqual(a, b) == 0;
    }

    private static ZipRange merge(ZipRange a, ZipRange b) {
        return new ZipRange(Math.min(a.getMin(), b.getMin()), Math.max(a.getMax(), b.getMax()));
    }
}
