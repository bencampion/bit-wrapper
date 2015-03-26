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
            (byte) 0b11111111,
            (byte) 0b00000000
    };
    private final Slicer slicer = new Slicer(ByteBuffer.wrap(bytes));

    @Test
    public void byteSlice_offset0Length0_returnsEmptyBuffer() {
        ByteBuffer slice = slicer.byteSlice(0, 0);
        assertThat(slice, is(ByteBuffer.allocate(0)));
    }

    @Test
    public void byteSlice_offset0Length1_returnsFirstByte() {
        ByteBuffer slice = slicer.byteSlice(0, 1);
        byte[] expected = {bytes[0]};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void byteSlice_offset0Length5_returnsBytes() {
        ByteBuffer slice = slicer.byteSlice(0, 5);
        assertThat(slice, is(ByteBuffer.wrap(bytes)));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void byteSlice_offset0Length6_throwsException() {
        slicer.byteSlice(0, 6);
    }

    @Test
    public void byteSlice_offset1Length2_returnsMiddle2Bytes() {
        ByteBuffer slice = slicer.byteSlice(1, 2);
        byte[] expected = {bytes[1], bytes[2]};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void byteSlice_offset3Length1_returnsLastByte() {
        ByteBuffer slice = slicer.byteSlice(3, 1);
        byte[] expected = {bytes[3]};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void byteSlice_offset5Length0_throwsException() {
        slicer.byteSlice(5, 0);
    }

    @Test
    public void bitSlice_offset0Length0_returnsEmptyBuffer() {
        ByteBuffer slice = slicer.bitSlice(0, 0);
        assertThat(slice, is(ByteBuffer.allocate(0)));
    }

    @Test
    public void bitSlice_offset0length1_returnsZero() {
        ByteBuffer slice = slicer.bitSlice(0, 1);
        byte[] expected = {0};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset0length40_returnsBytes() {
        ByteBuffer slice = slicer.bitSlice(0, 40);
        assertThat(slice, is(ByteBuffer.wrap(bytes)));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void bitSlice_offset0Length41_throwsException() {
        slicer.bitSlice(0, 41);
    }

    @Test
    public void bitSlice_offset1length2_returnsOnes() {
        ByteBuffer slice = slicer.bitSlice(1, 2);
        byte[] expected = {0b11};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset3length3_returnsZeros() {
        ByteBuffer slice = slicer.bitSlice(3, 3);
        byte[] expected = {0};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset6length5_returnsOnes() {
        ByteBuffer slice = slicer.bitSlice(6, 5);
        byte[] expected = {0b11111};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset11length8_returnsZeros() {
        ByteBuffer slice = slicer.bitSlice(11, 8);
        byte[] expected = {0};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset19length13_returnsOnes() {
        ByteBuffer slice = slicer.bitSlice(19, 13);
        byte[] expected = {0b11111, (byte) 0b11111111};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset19length14_returnsOnesZero() {
        ByteBuffer slice = slicer.bitSlice(19, 14);
        byte[] expected = {0b111111, (byte) 0b11111110};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset31Length1_returnsOne() {
        ByteBuffer slice = slicer.bitSlice(31, 1);
        byte[] expected = {1};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset31Length2_returnsOneZero() {
        ByteBuffer slice = slicer.bitSlice(31, 2);
        byte[] expected = {0b10};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset32Length1_returnsZero() {
        ByteBuffer slice = slicer.bitSlice(32, 1);
        byte[] expected = {0};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void bitSlice_offset40Length0_throwsException() {
        slicer.bitSlice(40, 0);
    }
}