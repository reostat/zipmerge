package org.zip;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Range of zip codes
 */
public class ZipRange {

    private final int min, max;

    /**
     * Create new zip codes range.
     * <code>max</code> must be greater than or equal to <code>min</code>.
     * @param min lower bound
     * @param max upper bound
     */
    public ZipRange(int min, int max) {
        if (max < min)
            throw new IllegalArgumentException("max zip must be greater than or equal to min zip");
        this.min = min;
        this.max = max;
    }

    /**
     * @return range lower bound
     */
    public int getMin() {
        return min;
    }

    /**
     * @return range upper bound
     */
    public int getMax() {
        return max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZipRange zipRange = (ZipRange) o;
        return min == zipRange.min &&
                max == zipRange.max;
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "ZipRange{", "}")
                .add(Integer.toString(min))
                .add(Integer.toString(max))
                .toString();
    }

}
