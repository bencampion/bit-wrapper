package uk.recurse.bitwrapper.decoder;

import java.lang.reflect.AnnotatedElement;
import java.nio.ByteBuffer;

import static com.google.common.base.Preconditions.checkArgument;

public class ShortDecoder implements Decoder<Short> {

    @Override
    public Short decode(ByteBuffer buffer, AnnotatedElement method) {
        checkArgument(buffer.limit() >= 1 && buffer.limit() <= 2,
                "Short length must be >= 1 and <= 2");
        if (buffer.limit() == 1) {
            return (short) (0xff & buffer.get());
        } else {
            return buffer.getShort();
        }
    }
}
