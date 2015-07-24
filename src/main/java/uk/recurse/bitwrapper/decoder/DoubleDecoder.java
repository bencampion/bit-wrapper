package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

public class DoubleDecoder implements Function<ByteBuffer, Double> {

    @Override
    public Double apply(ByteBuffer buffer) {
        checkArgument(buffer.limit() == 8, "Double length must be 8");
        return buffer.getDouble();
    }
}
