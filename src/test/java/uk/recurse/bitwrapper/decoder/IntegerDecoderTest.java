package uk.recurse.bitwrapper.decoder;

import org.junit.Test;

import java.nio.ByteBuffer;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IntegerDecoderTest {

    private final IntegerDecoder decoder = new IntegerDecoder();

    @Test(expected = IllegalArgumentException.class)
    public void decode_emptyBuffer_throwsException() {
        decoder.decode(ByteBuffer.allocate(0), null, null);
    }

    @Test
    public void decode_bufferSize1_returnsByte() {
        byte[] bytes = {-1};
        Integer i = decoder.decode(ByteBuffer.wrap(bytes), null, null);
        assertThat(i, is(255));
    }

    @Test
    public void decode_bufferSize4_returnsAllBytes() {
        byte[] bytes = {Byte.MIN_VALUE, 0, 0, 0};
        Integer i = decoder.decode(ByteBuffer.wrap(bytes), null, null);
        assertThat(i, is(Integer.MIN_VALUE));
    }

    @Test
    public void decode_littleEndianBuffer_returnsBytesInCorrectOrder() {
        byte[] bytes = {0, 0, 0, Byte.MIN_VALUE};
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(LITTLE_ENDIAN);
        Integer i = decoder.decode(buffer, null, null);
        assertThat(i, is(Integer.MIN_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decode_bufferSize5_throwsException() {
        decoder.decode(ByteBuffer.allocate(5), null, null);
    }
}