package uk.recurse.bitwrapper.decoder;

import java.lang.reflect.AnnotatedElement;
import java.nio.ByteBuffer;

import static com.google.common.base.Preconditions.checkArgument;

public class BooleanDecoder implements Decoder<Boolean> {

    @Override
    public Boolean decode(ByteBuffer buffer, AnnotatedElement method) {
        checkArgument(buffer.limit() == 1, "Boolean length must be 1");
        return buffer.get() == 1;
    }
}
