package uk.recurse.bitwrapper.decoder;

import org.junit.Test;

import java.nio.ByteBuffer;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LongDecoderTest {

    private final LongDecoder decoder = new LongDecoder();

    @Test(expected = IllegalArgumentException.class)
    public void apply_emptyBuffer_throwsException() {
        decoder.apply(ByteBuffer.allocate(0));
    }

    @Test
    public void apply_bufferSize1_returnsByte() {
        byte[] bytes = {-1};
        Long i = decoder.apply(ByteBuffer.wrap(bytes));
        assertThat(i, is(255l));
    }

    @Test
    public void apply_bufferSize8_returnsAllBytes() {
        byte[] bytes = {Byte.MIN_VALUE, 0, 0, 0, 0, 0, 0, 0};
        Long i = decoder.apply(ByteBuffer.wrap(bytes));
        assertThat(i, is(Long.MIN_VALUE));
    }

    @Test
    public void apply_littleEndianBuffer_returnsBytesInCorrectOrder() {
        byte[] bytes = {0, 0, 0, 0, 0, 0, 0, Byte.MIN_VALUE};
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(LITTLE_ENDIAN);
        Long i = decoder.apply(buffer);
        assertThat(i, is(Long.MIN_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void apply_bufferSize9_throwsException() {
        decoder.apply(ByteBuffer.allocate(9));
    }
}
