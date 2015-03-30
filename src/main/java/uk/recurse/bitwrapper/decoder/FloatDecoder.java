package uk.recurse.bitwrapper.decoder;

import uk.recurse.bitwrapper.Wrapper;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;

import static com.google.common.base.Preconditions.checkArgument;

public class FloatDecoder implements Decoder<Float> {

    @Override
    public Float decode(ByteBuffer buffer, Method method, Wrapper wrapper) {
        checkArgument(buffer.limit() == 4, "Float length must be 4");
        return buffer.getFloat();
    }
}
