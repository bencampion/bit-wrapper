package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * UTF-16 character decoder function.
 */
public class CharacterDecoder implements Function<ByteBuffer, Character> {

    /**
     * Converts a ByteBuffer into a Character. If the buffer contains only one byte then it is treated as being the
     * least significant bits of the Character. This method respects the endianness of the buffer.
     *
     * @return the decoded Character
     * @throws IllegalArgumentException if the buffer does not contain exactly 1 or 2 bytes
     */
    @Override
    public Character apply(ByteBuffer buffer) {
        checkArgument(buffer.limit() >= 1 && buffer.limit() <= 2,
                "Character length must be >= 1 and <= 2");
        if (buffer.limit() == 1) {
            return (char) (0xff & buffer.get());
        } else {
            return buffer.getChar();
        }
    }
}
