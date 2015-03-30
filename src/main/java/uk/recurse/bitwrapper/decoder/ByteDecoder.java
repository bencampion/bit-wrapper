package uk.recurse.bitwrapper.decoder;

import uk.recurse.bitwrapper.Wrapper;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;

import static com.google.common.base.Preconditions.checkArgument;

public class ByteDecoder implements Decoder<Byte> {

    @Override
    public Byte decode(ByteBuffer buffer, Method method, Wrapper wrapper) {
        checkArgument(buffer.limit() == 1, "Byte length must be 1");
        return buffer.get();
    }
}
