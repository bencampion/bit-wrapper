package uk.recurse.bitwrapper.decoder;

import java.lang.reflect.AnnotatedElement;
import java.nio.ByteBuffer;

import static com.google.common.base.Preconditions.checkArgument;

public class DoubleDecoder implements Decoder<Double> {

    @Override
    public Double decode(ByteBuffer buffer, AnnotatedElement method) {
        checkArgument(buffer.limit() == 8, "Double length must be 8");
        return buffer.getDouble();
    }
}
