package uk.recurse.bitwrapper.decoder;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class StringDecoderTest {

    @Test
    public void decode_emptyBuffer_returnsEmptyString() {
        StringDecoder decoder = new StringDecoder();
        String s = decoder.decode(ByteBuffer.allocate(0), null, null);
        assertTrue(s.isEmpty());
    }

    @Test
    public void decode_ascii_returnsAsciiDecodedString() {
        StringDecoder decoder = new StringDecoder();
        byte[] bytes = {'&', ':'};
        String s = decoder.decode(ByteBuffer.wrap(bytes), null, null);
        assertThat(s, is("&:"));
    }

    @Test
    public void decode_utf16_returnsUtf16DecodedString() {
        StringDecoder decoder = new StringDecoder(StandardCharsets.UTF_16);
        byte[] bytes = {'&', ':'};
        String s = decoder.decode(ByteBuffer.wrap(bytes), null, null);
        assertThat(s, is("☺"));
    }
}