package uk.recurse.bitwrapper.decoder;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ByteArrayDecoderTest {

    private final ByteArrayDecoder decoder = new ByteArrayDecoder();

    @Test
    public void apply_bytes_returnsByteArray() {
        byte[] bytes = {1, 2, 3};
        byte[] decoded = decoder.apply(ByteBuffer.wrap(bytes));
        assertThat(decoded, is(bytes));
    }
}