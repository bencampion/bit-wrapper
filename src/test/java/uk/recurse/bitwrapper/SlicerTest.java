package uk.recurse.bitwrapper;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SlicerTest {

    private final byte[] bytes = {
            (byte) 0b01100011,
            (byte) 0b11100000,
            (byte) 0b00011111,
            (byte) 0b11111111
    };
    private final Slicer slicer = new Slicer(ByteBuffer.wrap(bytes));

    @Test(expected = IllegalArgumentException.class)
    public void byteSlice_offset0Length5_throwsException() {
        slicer.byteSlice(0, 5);
    }

    @Test
    public void byteSlice_offset0Length0_returnsEmptyBuffer() {
        ByteBuffer slice = slicer.byteSlice(0, 0);
        assertThat(slice, is(ByteBuffer.allocate(0)));
    }

    @Test
    public void byteSlice_offset0Length1_returnsFirstByte() {
        ByteBuffer slice = slicer.byteSlice(0, 1);
        ByteBuffer expected = ByteBuffer.allocate(1).put(0, bytes[0]);
        assertThat(slice, is(expected));
    }

    @Test
    public void byteSlice_offset1Length2_returnsMiddle2Bytes() {
        ByteBuffer slice = slicer.byteSlice(1, 2);
        ByteBuffer expected = ByteBuffer.allocate(2).put(0, bytes[1]).put(1, bytes[2]);
        assertThat(slice, is(expected));
    }

    @Test
    public void byteSlice_offset3Length1_returnsLastByte() {
        ByteBuffer slice = slicer.byteSlice(3, 1);
        ByteBuffer expected = ByteBuffer.allocate(1).put(0, bytes[3]);
        assertThat(slice, is(expected));
    }

    @Test(expected = IllegalArgumentException.class)
    public void byteSlice_offset4Length0_throwsException() {
        slicer.byteSlice(5, 1);
    }
}