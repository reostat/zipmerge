package org.zip;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Test all implementations against known set of scenarios.
 * <p/>
 * This class holds the tests, see {@link MergerTestData} for collection of test scenarios
 */
@RunWith(Parameterized.class)
public class MergerTest extends MergerTestData {

    @Test
    public void testListMerger() {
        testFor(ZipRangeMergerFactory.Strategy.LIST);
    }

    @Test
    public void testTreeMerger() {
        testFor(ZipRangeMergerFactory.Strategy.TREE);
    }

    @Test
    public void testParallelTreeMerger() {
        testFor(ZipRangeMergerFactory.Strategy.PARALLEL_TREE);
    }

}
