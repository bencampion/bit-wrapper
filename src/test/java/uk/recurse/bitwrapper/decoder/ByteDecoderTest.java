package uk.recurse.bitwrapper.decoder;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ByteDecoderTest {

    private final ByteDecoder decoder = new ByteDecoder();

    @Test(expected = IllegalArgumentException.class)
    public void decode_emptyBuffer_throwsException() {
        decoder.decode(ByteBuffer.allocate(0), null);
    }

    @Test
    public void decode_bufferSize1_returnsByte() {
        byte[] bytes = {Byte.MAX_VALUE};
        Byte i = decoder.decode(ByteBuffer.wrap(bytes), null);
        assertThat(i, is(Byte.MAX_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decode_bufferSize2_throwsException() {
        decoder.decode(ByteBuffer.allocate(2), null);
    }
}