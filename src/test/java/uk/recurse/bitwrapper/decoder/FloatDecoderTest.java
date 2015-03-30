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
    public void decode_bufferSize3_throwsException() {
        decoder.decode(ByteBuffer.allocate(3), null, null);
    }

    @Test
    public void decode_bufferSize4_returnsBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putFloat(0, (float) Math.PI);
        Float f = decoder.decode(buffer, null, null);
        assertThat(f, is((float) Math.PI));
    }

    @Test
    public void decode_littleEndianBuffer_returnsBytesInCorrectOrder() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putFloat(0, (float) Math.PI);
        Collections.reverse(Bytes.asList(buffer.array()));
        Float f = decoder.decode(buffer.order(LITTLE_ENDIAN), null, null);
        assertThat(f, is((float) Math.PI));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decode_bufferSize5_throwsException() {
        decoder.decode(ByteBuffer.allocate(5), null, null);
    }
}