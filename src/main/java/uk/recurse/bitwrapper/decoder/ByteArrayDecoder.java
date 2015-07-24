package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.util.function.Function;

/**
 * Byte array decoder function.
 */
public class ByteArrayDecoder implements Function<ByteBuffer, byte[]> {

    /**
     * Converts a ByteBuffer into a byte array. This method always copies the buffer data into a new byte array even if
     * the buffer is backed by a byte array of the same length.
     *
     * @return a byte array containing all of the bytes in the buffer
     */
    @Override
    public byte[] apply(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        return bytes;
    }
}
