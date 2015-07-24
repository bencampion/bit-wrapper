package uk.recurse.bitwrapper;

import org.junit.Before;
import org.junit.Test;
import uk.recurse.bitwrapper.annotation.Bytes;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PngChunkTest {

    private static final String TYPE = "IEND";
    private static final byte[] CRC = {(byte) 0xae, 0x42, 0x60, (byte) 0x82};

    private PngChunk pngChunk;

    @Before
    public void setup() {
        ByteBuffer buffer = ByteBuffer.allocate(15);
        buffer.putInt(0);
        buffer.put(TYPE.getBytes(StandardCharsets.US_ASCII));
        buffer.put(CRC);
        buffer.rewind();
        pngChunk = Wrapper.create().wrap(buffer, PngChunk.class);
    }

    @Test
    public void length() {
        assertThat(pngChunk.length(), is(0));
    }

    @Test
    public void type() {
        assertThat(pngChunk.type(), is(TYPE));
    }

    @Test
    public void data() {
        assertThat(pngChunk.data(), is(ByteBuffer.allocate(0)));
    }

    @Test
    public void crc() {
        assertThat(pngChunk.crc(), is(CRC));
    }

    public interface PngChunk {

        @Bytes(length = 4)
        int length();

        @Bytes(offset = 4, length = 4)
        String type();

        @Bytes(offset = 8, lengthExp = "length()")
        ByteBuffer data();

        @Bytes(offsetExp = "length() + 8", length = 4)
        byte[] crc();
    }
}
