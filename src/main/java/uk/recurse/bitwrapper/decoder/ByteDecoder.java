package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Byte decoder function.
 */
public class ByteDecoder implements Function<ByteBuffer, Byte> {

    /**
     * Converts a ByteBuffer into a Byte.
     *
     * @return the byte in the buffer
     * @throws IllegalArgumentException if the buffer does not contain exactly one byte
     */
    @Override
    public Byte apply(ByteBuffer buffer) {
        checkArgument(buffer.limit() == 1, "Byte length must be 1");
        return buffer.get();
    }
}
