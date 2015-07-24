package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

public class BooleanDecoder implements Function<ByteBuffer, Boolean> {

    @Override
    public Boolean apply(ByteBuffer buffer) {
        checkArgument(buffer.limit() == 1, "Boolean length must be 1");
        return buffer.get() == 1;
    }
}
