package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.nio.ByteOrder.BIG_ENDIAN;

/**
 * Integer decoder function.
 */
public class IntegerDecoder implements Function<ByteBuffer, Integer> {

    /**
     * Converts a ByteBuffer into an Integer. If the buffer contains less than four bytes then the bytes are treated as
     * being the least significant bits of the Integer. This method respects the endianness of the buffer.
     *
     * @return the decoded Integer
     * @throws IllegalArgumentException if the buffer does not contain between 1 and 4 bytes inclusive
     */
    @Override
    public Integer apply(ByteBuffer buffer) {
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
