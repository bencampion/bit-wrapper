package uk.recurse.bitwrapper.decoder;

import uk.recurse.bitwrapper.Wrapper;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;

import static com.google.common.base.Preconditions.checkArgument;

public class DoubleDecoder implements Decoder<Double> {

    @Override
    public Double decode(ByteBuffer buffer, Method method, Wrapper wrapper) {
        checkArgument(buffer.limit() == 8, "Double length must be 8");
        return buffer.getDouble();
    }
}
