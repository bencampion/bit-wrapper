package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Short decoder function.
 */
public class ShortDecoder implements Function<ByteBuffer, Short> {

    /**
     * Converts a ByteBuffer into a Short. If the buffer contains only one byte then it is treated as being the least
     * significant bits of the Short. This method respects the endianness of the buffer.
     *
     * @return the decoded Short
     * @throws IllegalArgumentException if the buffer does not contain exactly 1 or 2 bytes
     */
    @Override
    public Short apply(ByteBuffer buffer) {
        checkArgument(buffer.limit() >= 1 && buffer.limit() <= 2,
                "Short length must be >= 1 and <= 2");
        if (buffer.limit() == 1) {
            return (short) (0xff & buffer.get());
        } else {
            return buffer.getShort();
        }
    }
}
