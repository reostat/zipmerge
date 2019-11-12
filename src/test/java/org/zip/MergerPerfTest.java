package org.zip;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * Performance comparison test
 */
public class MergerPerfTest {

    private static class Merger {
        private ZipRangeMerger merger;
        private ZipRangeMergerFactory.Strategy strategy;

        private Merger(ZipRangeMerger merger, ZipRangeMergerFactory.Strategy strategy) {
            this.merger = merger;
            this.strategy = strategy;
        }
    }

    private static final Iterable<List<ZipRange>> data = IntStream.of(10, 100, 1_000, 10_000, 100_000, 1_000_000)
            .mapToObj(MergerPerfTest::generatePairs)::iterator;

    private static final List<Merger> mergers = EnumSet.allOf(ZipRangeMergerFactory.Strategy.class).stream()
            .map(strategy -> new Merger(ZipRangeMergerFactory.mergerFor(strategy), strategy))
            .collect(Collectors.toList());

    @Test
    public void runPerf() {

        System.out.println("-----------------------------------------------------------------------");
        System.out.println(String.format("| %-13s | %7s | %7s | %-14s | %-14s |",
                "ALGORITHM", "SIZE", "MERGES", "TOTAL", "AVERAGE"));
        System.out.println("-----------------------------------------------------------------------");

        for (List<ZipRange> datum : data) {
            ArrayList<Collection<ZipRange>> results = new ArrayList<>();

            for (Merger merger : mergers) {
                Collection<ZipRange> copy = new ArrayList<>(datum);
                results.add(runPerf(merger, copy));
            }

            for (int i = 0; i < results.size() - 1; i++) {
                assertThat(results.get(i), contains(results.get(i + 1).toArray()));
            }
            System.out.println("-----------------------------------------------------------------------");
        }

    }

    private static Collection<ZipRange> runPerf(Merger merger, Collection<ZipRange> datum) {
        Instant start = Instant.now();
        Collection<ZipRange> result = merger.merger.merge(datum);
        Instant stop = Instant.now();

        Duration duration = Duration.between(start, stop);
        Duration avg = duration.dividedBy(datum.size());
        int merged = datum.size() - result.size();

        System.out.println(String.format("| %-13s | %7d | %7d | %-14s | %-14s |",
                merger.strategy.name(), datum.size(), merged, duration, avg));

        return result;
    }

    private static List<ZipRange> generatePairs(int num) {
        return IntStream.range(0, num)
                .mapToObj(i -> generatePair(num))
                .collect(Collectors.toList());
    }

    private static ZipRange generatePair(int upTo) {
        int low = randomBetweenInclusive(0, upTo);
        return new ZipRange(low, randomBetweenInclusive(low, low + 10));
    }

    private static int randomBetweenInclusive(int low, int high) {
        double f = Math.random() / Math.nextDown(1.0);
        return (int) (low * (1.0 - f) + high * f);
    }
}
