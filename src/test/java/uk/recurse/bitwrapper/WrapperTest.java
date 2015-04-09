package uk.recurse.bitwrapper;

import org.junit.Test;
import uk.recurse.bitwrapper.decoder.Decoder;

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class WrapperTest {

    private final Wrapper wrapper = Wrapper.create();

    @Test(expected = NullPointerException.class)
    public void wrapArray_nullArray_throwsException() {
        wrapper.wrap((byte[]) null, CharSequence.class);
    }

    @Test(expected = NullPointerException.class)
    public void wrapBuffer_nullBuffer_throwsException() {
        wrapper.wrap((ByteBuffer) null, CharSequence.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrapBuffer_wrapConcreteClass_throwsException() {
        wrapper.wrap(ByteBuffer.allocate(0), String.class);
    }

    @Test
    public void builder_addDecoder_addsDecoderToWrapper() {
        Decoder<Object> decoder = (buffer, method, wrapper) -> new Object();
        Wrapper wrapper = Wrapper.builder().addDecoder(Object.class, decoder).build();
        assertThat(wrapper.getDecoder(Object.class), sameInstance(decoder));
    }

    @Test
    public void builder_addPrimitiveDecoder_addsDecoderToWrapperForPrimitiveAndWrappedTypes() {
        Decoder<Integer> decoder = (buffer, method, wrapper) -> 0;
        Wrapper wrapper = Wrapper.builder().addDecoder(Integer.class, decoder).build();
        assertThat(wrapper.getDecoder(Integer.class), sameInstance(decoder));
        assertThat(wrapper.getDecoder(int.class), sameInstance(decoder));
    }
}
