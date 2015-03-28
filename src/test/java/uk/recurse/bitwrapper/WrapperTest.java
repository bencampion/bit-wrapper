package uk.recurse.bitwrapper;

import org.junit.Test;
import uk.recurse.bitwrapper.decoder.Decoder;

import java.lang.reflect.AnnotatedElement;
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
        Decoder<Object> decoder = new Decoder<Object>() {
            @Override
            public Object decode(ByteBuffer buffer, AnnotatedElement method) {
                return null;
            }
        };
        Wrapper wrapper = Wrapper.builder().addDecoder(decoder).build();
        assertThat(wrapper.getDecoder(Object.class), sameInstance(decoder));
    }

    @Test
    public void builder_addPrimitiveDecoder_addsDecoderToWrapperForPrimitiveAndWrappedTypes() {
        Decoder<Integer> decoder = new Decoder<Integer>() {
            @Override
            public Integer decode(ByteBuffer buffer, AnnotatedElement method) {
                return null;
            }
        };
        Wrapper wrapper = Wrapper.builder().addDecoder(decoder).build();
        assertThat(wrapper.getDecoder(Integer.class), sameInstance(decoder));
        assertThat(wrapper.getDecoder(int.class), sameInstance(decoder));
    }
}
