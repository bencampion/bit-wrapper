package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

public class ShortDecoder implements Function<ByteBuffer, Short> {

    @Override
    public Short apply(ByteBuffer buffer) {
        checkArgument(buffer.limit() >= 1 && buffer.limit() <= 2,
                "Short length must be >= 1 and <= 2");
        if (buffer.limit() == 1) {
            return (short) (0xff & buffer.get());
        } else {
            return buffer.getShort();
        }
    }
}
