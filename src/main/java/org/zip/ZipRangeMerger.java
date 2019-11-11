package org.zip;

import java.util.Collection;

/**
 * Interface for zip range merger implementation
 */
@FunctionalInterface
public interface ZipRangeMerger {

    /**
     * Process collection of ranges; merge those that overlap
     * @param ranges collection of zip ranges. Any order, any number of overlaps
     * @return collection with overlapping ranges merged.
     *      Implementations must guarantee that returned ranges are sorted by lower bound in ascending order.
     *      Implementations may or may not reuse input collection for in-place merge.
     */
    Collection<ZipRange> merge(Collection<ZipRange> ranges);

}
