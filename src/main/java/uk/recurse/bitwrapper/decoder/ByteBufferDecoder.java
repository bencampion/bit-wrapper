package uk.recurse.bitwrapper.decoder;

import java.lang.reflect.AnnotatedElement;
import java.nio.ByteBuffer;

import static com.google.common.base.Preconditions.checkArgument;

public class ByteBufferDecoder implements Decoder<ByteBuffer> {

    @Override
    public ByteBuffer decode(ByteBuffer buffer, AnnotatedElement method) {
        return buffer;
    }
}
