package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class StringDecoder implements Function<ByteBuffer, String> {

    private final Charset charset;

    public StringDecoder() {
        this(StandardCharsets.US_ASCII);
    }

    public StringDecoder(Charset charset) {
        this.charset = charset;
    }

    @Override
    public String apply(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        return new String(bytes, charset);
    }
}
