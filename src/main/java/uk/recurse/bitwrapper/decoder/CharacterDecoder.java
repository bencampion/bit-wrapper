package uk.recurse.bitwrapper.decoder;

import java.nio.ByteBuffer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

public class CharacterDecoder implements Function<ByteBuffer, Character> {

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
