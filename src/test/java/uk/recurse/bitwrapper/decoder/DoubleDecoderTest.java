package uk.recurse.bitwrapper.decoder;

import com.google.common.primitives.Bytes;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Collections;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DoubleDecoderTest {

    private final DoubleDecoder decoder = new DoubleDecoder();

    @Test(expected = IllegalArgumentException.class)
    public void apply_bufferSize7_throwsException() {
        decoder.apply(ByteBuffer.allocate(7));
    }

    @Test
    public void apply_bufferSize8_returnsBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putDouble(0, Math.PI);
        Double f = decoder.apply(buffer);
        assertThat(f, is(Math.PI));
    }

    @Test
    public void apply_littleEndianBuffer_returnsBytesInCorrectOrder() {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putDouble(0, Math.PI);
        Collections.reverse(Bytes.asList(buffer.array()));
        Double f = decoder.apply(buffer.order(LITTLE_ENDIAN));
        assertThat(f, is(Math.PI));
    }

    @Test(expected = IllegalArgumentException.class)
    public void apply_bufferSize9_throwsException() {
        decoder.apply(ByteBuffer.allocate(9));
    }
}
