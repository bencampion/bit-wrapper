package uk.recurse.bitwrapper.decoder;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class ByteBufferDecoderTest {

    private final ByteBufferDecoder decoder = new ByteBufferDecoder();

    @Test
    public void apply_buffer_returnsSameInstance() {
        ByteBuffer buffer = ByteBuffer.allocate(0);
        ByteBuffer decoded = decoder.apply(buffer);
        assertThat(decoded, sameInstance(buffer));
    }
}
