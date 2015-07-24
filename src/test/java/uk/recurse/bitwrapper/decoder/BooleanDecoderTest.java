package uk.recurse.bitwrapper.decoder;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BooleanDecoderTest {

    private final BooleanDecoder decoder = new BooleanDecoder();

    @Test(expected = IllegalArgumentException.class)
    public void apply_emptyBuffer_throwsException() {
        decoder.apply(ByteBuffer.allocate(0));
    }

    @Test
    public void apply_bufferWithLastBitSet_returnsTrue() {
        byte[] bytes = {1};
        Boolean b = decoder.apply(ByteBuffer.wrap(bytes));
        assertTrue(b);
    }

    @Test
    public void apply_bufferWithLastBitClear_returnsFalse() {
        byte[] bytes = {-2};
        Boolean b = decoder.apply(ByteBuffer.wrap(bytes));
        assertFalse(b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void apply_bufferSize2_throwsException() {
        decoder.apply(ByteBuffer.allocate(2));
    }
}
