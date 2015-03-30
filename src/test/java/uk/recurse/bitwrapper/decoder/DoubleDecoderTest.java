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
    public void decode_bufferSize7_throwsException() {
        decoder.decode(ByteBuffer.allocate(7), null, null);
    }

    @Test
    public void decode_bufferSize8_returnsBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putDouble(0, Math.PI);
        Double f = decoder.decode(buffer, null, null);
        assertThat(f, is(Math.PI));
    }

    @Test
    public void decode_littleEndianBuffer_returnsBytesInCorrectOrder() {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putDouble(0, Math.PI);
        Collections.reverse(Bytes.asList(buffer.array()));
        Double f = decoder.decode(buffer.order(LITTLE_ENDIAN), null, null);
        assertThat(f, is(Math.PI));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decode_bufferSize9_throwsException() {
        decoder.decode(ByteBuffer.allocate(9), null, null);
    }
}