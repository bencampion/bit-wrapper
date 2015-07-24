package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Boolean decoder function.
 */
public class BooleanDecoder implements Function<ByteBuffer, Boolean> {

    /**
     * Converts a ByteBuffer into a Boolean.
     *
     * @return true if the buffer byte equals 1
     * @throws IllegalArgumentException if the buffer does not contain exactly 1 byte
     */
    @Override
    public Boolean apply(ByteBuffer buffer) {
        checkArgument(buffer.limit() == 1, "Boolean length must be 1");
        return buffer.get() == 1;
    }
}
