package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.util.function.Function;

public class ByteBufferDecoder implements Function<ByteBuffer, ByteBuffer> {

    @Override
    public ByteBuffer apply(ByteBuffer buffer) {
        return buffer;
    }
}
