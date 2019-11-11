package org.zip;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

public class ZipRangeTest {

    @Test
    public void testCtorPositive() {
        new ZipRange(12, 15);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCtorNegative() {
        new ZipRange(15, 12);
    }

    @Test
    public void testEqualsPositive() {
        ZipRange pair1 = new ZipRange(23, 45);
        ZipRange pair2 = new ZipRange(23, 45);
        assertThat(pair1.equals(pair2), is(true));
    }

    @Test
    public void testEqualsNegative() {
        ZipRange pair1 = new ZipRange(23, 45);
        ZipRange pair2 = new ZipRange(14, 45);
        ZipRange pair3 = new ZipRange(23, 40);
        ZipRange pair4 = new ZipRange(14, 40);
        assertThat(pair1.equals(pair2), is(false));
        assertThat(pair1.equals(pair3), is(false));
        assertThat(pair1.equals(pair4), is(false));
    }

}
