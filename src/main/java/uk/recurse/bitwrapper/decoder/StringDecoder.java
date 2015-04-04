package uk.recurse.bitwrapper.decoder;

import uk.recurse.bitwrapper.Wrapper;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringDecoder implements Decoder<String> {

    private final Charset charset;

    public StringDecoder() {
        this(StandardCharsets.US_ASCII);
    }

    public StringDecoder(Charset charset) {
        this.charset = charset;
    }

    @Override
    public String decode(ByteBuffer buffer, Method method, Wrapper wrapper) {
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        return new String(bytes, charset);
    }
}
