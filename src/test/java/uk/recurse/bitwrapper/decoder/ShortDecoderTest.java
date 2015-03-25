package uk.recurse.bitwrapper.decoder;

import org.junit.Test;

import java.nio.ByteBuffer;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ShortDecoderTest {

    private final ShortDecoder decoder = new ShortDecoder();

    @Test(expected = IllegalArgumentException.class)
    public void decode_emptyBuffer_throwsException() {
        decoder.decode(ByteBuffer.allocate(0), null);
    }

    @Test
    public void decode_bufferSize1_returnsByte() {
        byte[] bytes = {-1};
        Short i = decoder.decode(ByteBuffer.wrap(bytes), null);
        assertThat(i, is((short) 255));
    }

    @Test
    public void decode_bufferSize2_returnsAllBytes() {
        byte[] bytes = {Byte.MIN_VALUE, 0};
        Short i = decoder.decode(ByteBuffer.wrap(bytes), null);
        assertThat(i, is(Short.MIN_VALUE));
    }

    @Test
    public void decode_littleEndianBuffer_returnsBytesInCorrectOrder() {
        byte[] bytes = {0, Byte.MIN_VALUE};
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(LITTLE_ENDIAN);
        Short i = decoder.decode(buffer, null);
        assertThat(i, is(Short.MIN_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decode_bufferSize3_throwsException() {
        decoder.decode(ByteBuffer.allocate(3), null);
    }
}