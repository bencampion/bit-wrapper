package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * String decoder function.
 */
public class StringDecoder implements Function<ByteBuffer, String> {

    private final Charset charset;

    /**
     * Constructs a decoder using the ASCII charset.
     */
    public StringDecoder() {
        this(StandardCharsets.US_ASCII);
    }

    /**
     * Constructs a decoder using a custom charset.
     */
    public StringDecoder(Charset charset) {
        this.charset = charset;
    }

    /**
     * Converts a ByteBuffer into a String.
     *
     * @return a String decoded using the charset provided during construction
     */
    @Override
    public String apply(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        return new String(bytes, charset);
    }
}
