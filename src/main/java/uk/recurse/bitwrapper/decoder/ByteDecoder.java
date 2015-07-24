package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

public class ByteDecoder implements Function<ByteBuffer, Byte> {

    @Override
    public Byte apply(ByteBuffer buffer) {
        checkArgument(buffer.limit() == 1, "Byte length must be 1");
        return buffer.get();
    }
}
