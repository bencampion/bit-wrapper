package uk.recurse.bitwrapper.decoder;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BooleanDecoderTest {

    private final BooleanDecoder decoder = new BooleanDecoder();

    @Test(expected = IllegalArgumentException.class)
    public void decode_emptyBuffer_throwsException() {
        decoder.decode(ByteBuffer.allocate(0), null, null);
    }

    @Test
    public void decode_bufferWithLastBitSet_returnsTrue() {
        byte[] bytes = {1};
        Boolean b = decoder.decode(ByteBuffer.wrap(bytes), null, null);
        assertTrue(b);
    }

    @Test
    public void decode_bufferWithLastBitClear_returnsFalse() {
        byte[] bytes = {-2};
        Boolean b = decoder.decode(ByteBuffer.wrap(bytes), null, null);
        assertFalse(b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void decode_bufferSize2_throwsException() {
        decoder.decode(ByteBuffer.allocate(2), null, null);
    }
}