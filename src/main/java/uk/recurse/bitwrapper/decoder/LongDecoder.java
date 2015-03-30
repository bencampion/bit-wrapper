package uk.recurse.bitwrapper.decoder;

import uk.recurse.bitwrapper.Wrapper;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;

import static com.google.common.base.Preconditions.checkArgument;
import static java.nio.ByteOrder.BIG_ENDIAN;

public class LongDecoder implements Decoder<Long> {

    @Override
    public Long decode(ByteBuffer buffer, Method method, Wrapper wrapper) {
        checkArgument(buffer.limit() >= 1 && buffer.limit() <= 8,
                "Long length must be >= 1 and <= 8");
        boolean bigEndian = buffer.order().equals(BIG_ENDIAN);
        long n = 0;
        for (int i = 0; i < buffer.limit(); i++) {
            int j = bigEndian ? buffer.limit() - i - 1 : i;
            n |= (buffer.get(j) & 0xffl) << (i * 8);
        }
        return n;
    }
}
