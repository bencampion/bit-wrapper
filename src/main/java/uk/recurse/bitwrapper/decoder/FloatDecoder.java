package uk.recurse.bitwrapper.decoder;

import java.lang.reflect.AnnotatedElement;
import java.nio.ByteBuffer;

import static com.google.common.base.Preconditions.checkArgument;

public class FloatDecoder implements Decoder<Float> {

    @Override
    public Float decode(ByteBuffer buffer, AnnotatedElement method) {
        checkArgument(buffer.limit() == 4, "Float length must be 4");
        return buffer.getFloat();
    }
}
