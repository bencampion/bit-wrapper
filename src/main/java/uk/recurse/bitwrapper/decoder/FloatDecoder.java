package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Float decoder function.
 */
public class FloatDecoder implements Function<ByteBuffer, Float> {

    /**
     * Converts a ByteBuffer into a Float.
     *
     * @return the decoded Float
     * @throws IllegalArgumentException if the buffer does not contain exactly 4 bytes
     */
    @Override
    public Float apply(ByteBuffer buffer) {
        checkArgument(buffer.limit() == 4, "Float length must be 4");
        return buffer.getFloat();
    }
}
