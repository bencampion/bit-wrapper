package uk.recurse.bitwrapper.decoder;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ByteDecoderTest {

    private final ByteDecoder decoder = new ByteDecoder();

    @Test(expected = IllegalArgumentException.class)
    public void apply_emptyBuffer_throwsException() {
        decoder.apply(ByteBuffer.allocate(0));
    }

    @Test
    public void apply_bufferSize1_returnsByte() {
        byte[] bytes = {Byte.MAX_VALUE};
        Byte i = decoder.apply(ByteBuffer.wrap(bytes));
        assertThat(i, is(Byte.MAX_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void apply_bufferSize2_throwsException() {
        decoder.apply(ByteBuffer.allocate(2));
    }
}
