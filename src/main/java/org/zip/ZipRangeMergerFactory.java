package org.zip;

/**
 * All implementations are package-private, this class should be used to obtain an instance.
 */
public class ZipRangeMergerFactory {

    /**
     * Merge implementation strategy
     */
    public enum Strategy {

        /**
         * List-based merge
         */
        LIST(ZipRangeListMerger::merge),

        /**
         * TreeSet-based merge
         */
        TREE(ZipRangeTreeMerger::treeMerge),

        /**
         * TreeSet-based merge, parallel
         */
        PARALLEL_TREE(ZipRangeTreeMerger::parallelTreeMerge);


        private final ZipRangeMerger merger;

        Strategy(ZipRangeMerger merger) {
            this.merger = merger;
        }
    }

    /**
     * @param strategy merge strategy to use
     * @return implementation of requested merge strategy
     */
    public static ZipRangeMerger mergerFor(Strategy strategy) {
        return strategy.merger;
    }

}
