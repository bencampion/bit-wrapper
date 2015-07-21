package uk.recurse.bitwrapper;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BufferSlicerTest {

    private final byte[] bytes = {
            (byte) 0b01100011,
            (byte) 0b11100000,
            (byte) 0b00011111,
            (byte) 0b11111111,
            (byte) 0b00000000
    };
    private final BufferSlicer bufferSlicer = new BufferSlicer(ByteBuffer.wrap(bytes));

    @Test
    public void byteSlice_offset0Length0_returnsEmptyBuffer() {
        ByteBuffer slice = bufferSlicer.byteSlice(0, 0);
        assertThat(slice, is(ByteBuffer.allocate(0)));
    }

    @Test
    public void byteSlice_offset0Length1_returnsFirstByte() {
        ByteBuffer slice = bufferSlicer.byteSlice(0, 1);
        byte[] expected = {bytes[0]};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void byteSlice_offset0Length5_returnsBytes() {
        ByteBuffer slice = bufferSlicer.byteSlice(0, 5);
        assertThat(slice, is(ByteBuffer.wrap(bytes)));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void byteSlice_offset0Length6_throwsException() {
        bufferSlicer.byteSlice(0, 6);
    }

    @Test
    public void byteSlice_offset1Length2_returnsMiddle2Bytes() {
        ByteBuffer slice = bufferSlicer.byteSlice(1, 2);
        byte[] expected = {bytes[1], bytes[2]};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void byteSlice_offset3Length1_returnsLastByte() {
        ByteBuffer slice = bufferSlicer.byteSlice(3, 1);
        byte[] expected = {bytes[3]};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void byteSlice_offset5Length0_returnsEmptyBuffer() {
        ByteBuffer slice = bufferSlicer.byteSlice(5, 0);
        assertThat(slice, is(ByteBuffer.allocate(0)));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void byteSlice_offset5Length1_throwsException() {
        bufferSlicer.byteSlice(5, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void byteSlice_offset6Length0_throwsException() {
        bufferSlicer.byteSlice(6, 0);
    }

    @Test
    public void bitSlice_offset0Length0_returnsEmptyBuffer() {
        ByteBuffer slice = bufferSlicer.bitSlice(0, 0);
        assertThat(slice, is(ByteBuffer.allocate(0)));
    }

    @Test
    public void bitSlice_offset0length1_returnsZero() {
        ByteBuffer slice = bufferSlicer.bitSlice(0, 1);
        byte[] expected = {0};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset0length40_returnsBytes() {
        ByteBuffer slice = bufferSlicer.bitSlice(0, 40);
        assertThat(slice, is(ByteBuffer.wrap(bytes)));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void bitSlice_offset0Length41_throwsException() {
        bufferSlicer.bitSlice(0, 41);
    }

    @Test
    public void bitSlice_offset1length2_returnsOnes() {
        ByteBuffer slice = bufferSlicer.bitSlice(1, 2);
        byte[] expected = {0b11};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset3length3_returnsZeros() {
        ByteBuffer slice = bufferSlicer.bitSlice(3, 3);
        byte[] expected = {0};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset6length5_returnsOnes() {
        ByteBuffer slice = bufferSlicer.bitSlice(6, 5);
        byte[] expected = {0b11111};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset11length8_returnsZeros() {
        ByteBuffer slice = bufferSlicer.bitSlice(11, 8);
        byte[] expected = {0};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset19length13_returnsOnes() {
        ByteBuffer slice = bufferSlicer.bitSlice(19, 13);
        byte[] expected = {0b11111, (byte) 0b11111111};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset19length14_returnsOnesZero() {
        ByteBuffer slice = bufferSlicer.bitSlice(19, 14);
        byte[] expected = {0b111111, (byte) 0b11111110};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset31Length1_returnsOne() {
        ByteBuffer slice = bufferSlicer.bitSlice(31, 1);
        byte[] expected = {1};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset31Length2_returnsOneZero() {
        ByteBuffer slice = bufferSlicer.bitSlice(31, 2);
        byte[] expected = {0b10};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset32Length8_returnsZero() {
        ByteBuffer slice = bufferSlicer.bitSlice(32, 8);
        byte[] expected = {0};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset39Length1_returnsZero() {
        ByteBuffer slice = bufferSlicer.bitSlice(39, 1);
        byte[] expected = {0};
        assertThat(slice, is(ByteBuffer.wrap(expected)));
    }

    @Test
    public void bitSlice_offset40Length0_returnsEmptyBuffer() {
        ByteBuffer slice = bufferSlicer.bitSlice(40, 0);
        assertThat(slice, is(ByteBuffer.allocate(0)));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void bitSlice_offset40Length1_throwsException() {
        bufferSlicer.bitSlice(40, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void bitSlice_offset41Length0_throwsException() {
        bufferSlicer.bitSlice(41, 0);
    }
}