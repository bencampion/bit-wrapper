package uk.recurse.bitwrapper.decoder;

import com.google.common.primitives.Chars;
import org.junit.Test;

import java.nio.ByteBuffer;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CharacterDecoderTest {

    private final CharacterDecoder decoder = new CharacterDecoder();

    @Test(expected = IllegalArgumentException.class)
    public void apply_bufferSize0_throwsException() {
        decoder.apply(ByteBuffer.allocate(0));
    }

    @Test
    public void apply_bufferSize1_returnsCharacter() {
        byte[] bytes = {(byte) 'µ'};
        Character c = decoder.apply(ByteBuffer.wrap(bytes));
        assertThat(c, is('µ'));
    }

    @Test
    public void apply_bufferSize2_returnsCharacter() {
        byte[] bytes = Chars.toByteArray('☺');
        Character c = decoder.apply(ByteBuffer.wrap(bytes));
        assertThat(c, is('☺'));
    }

    @Test
    public void apply_littleEndianBuffer_returnsBytesInCorrectOrder() {
        byte[] bytes = Chars.toByteArray(Character.reverseBytes('☺'));
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(LITTLE_ENDIAN);
        Character c = decoder.apply(buffer);
        assertThat(c, is('☺'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void apply_bufferSize3_throwsException() {
        decoder.apply(ByteBuffer.allocate(3));
    }
}
