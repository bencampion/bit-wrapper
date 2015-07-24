package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.util.function.Function;

/**
 * ByteBuffer decoder function. This is an identity function.
 *
 * @see Function#identity()
 */
public class ByteBufferDecoder implements Function<ByteBuffer, ByteBuffer> {

    /**
     * Returns the input buffer.
     *
     * @return the input argument
     */
    @Override
    public ByteBuffer apply(ByteBuffer buffer) {
        return buffer;
    }
}
