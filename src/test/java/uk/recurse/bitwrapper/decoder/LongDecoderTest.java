package uk.recurse.bitwrapper.decoder;

import org.junit.Test;

import java.nio.ByteBuffer;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LongDecoderTest {

    private final LongDecoder decoder = new LongDecoder();

    @Test(expected = IllegalArgumentException.class)
    public void decode_emptyBuffer_throwsException() {
        decoder.decode(ByteBuffer.allocate(0), null, null);
    }

    @Test
    public void decode_bufferSize1_returnsByte() {
        byte[] bytes = {-1};
        Long i = decoder.decode(ByteBuffer.wrap(bytes), null, null);
        assertThat(i, is(255l));
    }

    @Test
    public void decode_bufferSize8_returnsAllBytes() {
        byte[] bytes = {Byte.MIN_VALUE, 0, 0, 0, 0, 0, 0, 0};
        Long i = decoder.decode(ByteBuffer.wrap(bytes), null, null);
        assertThat(i, is(Long.MIN_VALUE));
    }

    @Test
    public void decode_littleEndianBuffer_returnsBytesInCorrectOrder() {
        byte[] bytes = {0, 0, 0, 0, 0, 0, 0, Byte.MIN_VALUE};
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(LITTLE_ENDIAN);
        Long i = decoder.decode(buffer, null, null);
        assertThat(i, is(Long.MIN_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decode_bufferSize9_throwsException() {
        decoder.decode(ByteBuffer.allocate(9), null, null);
    }
}