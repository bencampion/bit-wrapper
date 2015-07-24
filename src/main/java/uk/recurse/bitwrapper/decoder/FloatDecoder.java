package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

public class FloatDecoder implements Function<ByteBuffer, Float> {

    @Override
    public Float apply(ByteBuffer buffer) {
        checkArgument(buffer.limit() == 4, "Float length must be 4");
        return buffer.getFloat();
    }
}
