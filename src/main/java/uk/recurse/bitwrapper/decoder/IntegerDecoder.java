package uk.recurse.bitwrapper.decoder;

import java.lang.reflect.AnnotatedElement;
import java.nio.ByteBuffer;

import static com.google.common.base.Preconditions.checkArgument;
import static java.nio.ByteOrder.BIG_ENDIAN;

public class IntegerDecoder implements Decoder<Integer> {

    @Override
    public Integer decode(ByteBuffer buffer, AnnotatedElement method) {
        checkArgument(buffer.limit() >= 1 && buffer.limit() <= 4,
                "Integer length must be >= 1 and <= 4");
        boolean bigEndian = buffer.order().equals(BIG_ENDIAN);
        int n = 0;
        for (int i = 0; i < buffer.limit(); i++) {
            int j = bigEndian ? buffer.limit() - i - 1 : i;
            n |= (buffer.get(j) & 0xff) << (i * 8);
        }
        return n;
    }
}
