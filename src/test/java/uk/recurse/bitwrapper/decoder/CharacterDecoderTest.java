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
    public void decode_bufferSize0_throwsException() {
        decoder.decode(ByteBuffer.allocate(0), null, null);
    }

    @Test
    public void decode_bufferSize1_returnsCharacter() {
        byte[] bytes = {(byte) 'µ'};
        Character c = decoder.decode(ByteBuffer.wrap(bytes), null, null);
        assertThat(c, is('µ'));
    }

    @Test
    public void decode_bufferSize2_returnsCharacter() {
        byte[] bytes = Chars.toByteArray('☺');
        Character c = decoder.decode(ByteBuffer.wrap(bytes), null, null);
        assertThat(c, is('☺'));
    }

    @Test
    public void decode_littleEndianBuffer_returnsBytesInCorrectOrder() {
        byte[] bytes = Chars.toByteArray(Character.reverseBytes('☺'));
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(LITTLE_ENDIAN);
        Character c = decoder.decode(buffer, null, null);
        assertThat(c, is('☺'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decode_bufferSize3_throwsException() {
        decoder.decode(ByteBuffer.allocate(3), null, null);
    }
}