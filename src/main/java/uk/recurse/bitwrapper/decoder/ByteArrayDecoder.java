package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.util.function.Function;

public class ByteArrayDecoder implements Function<ByteBuffer, byte[]> {

    @Override
    public byte[] apply(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        return bytes;
    }
}
