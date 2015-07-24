package uk.recurse.bitwrapper.decoder;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class StringDecoderTest {

    @Test
    public void apply_emptyBuffer_returnsEmptyString() {
        StringDecoder decoder = new StringDecoder();
        String s = decoder.apply(ByteBuffer.allocate(0));
        assertTrue(s.isEmpty());
    }

    @Test
    public void apply_ascii_returnsAsciiDecodedString() {
        StringDecoder decoder = new StringDecoder();
        byte[] bytes = {'&', ':'};
        String s = decoder.apply(ByteBuffer.wrap(bytes));
        assertThat(s, is("&:"));
    }

    @Test
    public void apply_utf16_returnsUtf16DecodedString() {
        StringDecoder decoder = new StringDecoder(StandardCharsets.UTF_16);
        byte[] bytes = {'&', ':'};
        String s = decoder.apply(ByteBuffer.wrap(bytes));
        assertThat(s, is("☺"));
    }
}
