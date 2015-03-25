package uk.recurse.bitwrapper.decoder;

import java.lang.reflect.AnnotatedElement;
import java.nio.ByteBuffer;

import static com.google.common.base.Preconditions.checkArgument;

public class ByteDecoder implements Decoder<Byte> {

    @Override
    public Byte decode(ByteBuffer buffer, AnnotatedElement method) {
        checkArgument(buffer.limit() == 1, "Byte length must be 1");
        return buffer.get();
    }
}
