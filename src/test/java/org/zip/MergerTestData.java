package org.zip;

import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * Test all implementations against known set of scenarios.
 * <p/>
 * This class holds collection of test scenarios, see {@link MergerTest} for actual tests
 */
public class MergerTestData {

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> switchCollectors() {
        return Arrays.asList(new Object[][]{
                        {"Overlap merge",
                                Arrays.asList(new ZipRange(12, 15), new ZipRange(13, 18)),
                                Arrays.asList(new ZipRange(12, 18))
                        },
                        {"No overlap merge",
                                Arrays.asList(new ZipRange(16, 18), new ZipRange(12, 15)),
                                Arrays.asList(new ZipRange(12, 15), new ZipRange(16, 18))
                        },
                        {"Skip merge",
                                Arrays.asList(new ZipRange(16, 18), new ZipRange(12, 15), new ZipRange(14, 16)),
                                Arrays.asList(new ZipRange(12, 18))
                        },
                        {"Inclusion merge",
                                Arrays.asList(new ZipRange(12, 18), new ZipRange(12, 15)),
                                Arrays.asList(new ZipRange(12, 18))
                        },
                        {"Sample 1",
                                Arrays.asList(new ZipRange(94133, 94133), new ZipRange(94200, 94299), new ZipRange(94600, 94699)),
                                Arrays.asList(new ZipRange(94133, 94133), new ZipRange(94200, 94299), new ZipRange(94600, 94699))
                        },
                        {"Sample 2",
                                Arrays.asList(new ZipRange(94133, 94133), new ZipRange(94200, 94299), new ZipRange(94226, 94399)),
                                Arrays.asList(new ZipRange(94133, 94133), new ZipRange(94200, 94399))
                        }
                }
        );
    }

    @Parameterized.Parameter(0)
    public String testName;

    @Parameterized.Parameter(1)
    public Collection<ZipRange> input;

    @Parameterized.Parameter(2)
    public Collection<ZipRange> expectedOutput;

    protected void testFor(ZipRangeMergerFactory.Strategy strategy) {
        Collection<ZipRange> mergedPairs = ZipRangeMergerFactory.mergerFor(strategy).merge(input);
        assertThat(mergedPairs, contains(expectedOutput.toArray()));
    }
}
