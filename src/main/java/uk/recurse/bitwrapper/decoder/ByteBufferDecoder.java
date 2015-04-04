package uk.recurse.bitwrapper.decoder;

import uk.recurse.bitwrapper.Wrapper;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public class ByteBufferDecoder implements Decoder<ByteBuffer> {

    @Override
    public ByteBuffer decode(ByteBuffer buffer, Method method, Wrapper wrapper) {
        return buffer;
    }
}
