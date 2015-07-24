package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Double decoder function.
 */
public class DoubleDecoder implements Function<ByteBuffer, Double> {

    /**
     * Converts a ByteBuffer into a Double.
     *
     * @return the decoded Double
     * @throws IllegalArgumentException if the buffer does not contain exactly 8 bytes
     */
    @Override
    public Double apply(ByteBuffer buffer) {
        checkArgument(buffer.limit() == 8, "Double length must be 8");
        return buffer.getDouble();
    }
}
