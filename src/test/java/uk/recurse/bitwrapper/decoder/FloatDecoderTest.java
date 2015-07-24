package uk.recurse.bitwrapper.decoder;

import com.google.common.primitives.Bytes;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Collections;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FloatDecoderTest {

    private final FloatDecoder decoder = new FloatDecoder();

    @Test(expected = IllegalArgumentException.class)
    public void apply_bufferSize3_throwsException() {
        decoder.apply(ByteBuffer.allocate(3));
    }

    @Test
    public void apply_bufferSize4_returnsBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putFloat(0, (float) Math.PI);
        Float f = decoder.apply(buffer);
        assertThat(f, is((float) Math.PI));
    }

    @Test
    public void apply_littleEndianBuffer_returnsBytesInCorrectOrder() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putFloat(0, (float) Math.PI);
        Collections.reverse(Bytes.asList(buffer.array()));
        Float f = decoder.apply(buffer.order(LITTLE_ENDIAN));
        assertThat(f, is((float) Math.PI));
    }

    @Test(expected = IllegalArgumentException.class)
    public void apply_bufferSize5_throwsException() {
        decoder.apply(ByteBuffer.allocate(5));
    }
}
