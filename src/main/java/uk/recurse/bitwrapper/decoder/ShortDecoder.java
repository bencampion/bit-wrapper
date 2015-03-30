package uk.recurse.bitwrapper.decoder;

import uk.recurse.bitwrapper.Wrapper;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;

import static com.google.common.base.Preconditions.checkArgument;

public class ShortDecoder implements Decoder<Short> {

    @Override
    public Short decode(ByteBuffer buffer, Method method, Wrapper wrapper) {
        checkArgument(buffer.limit() >= 1 && buffer.limit() <= 2,
                "Short length must be >= 1 and <= 2");
        if (buffer.limit() == 1) {
            return (short) (0xff & buffer.get());
        } else {
            return buffer.getShort();
        }
    }
}
