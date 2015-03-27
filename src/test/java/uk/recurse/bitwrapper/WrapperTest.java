package uk.recurse.bitwrapper;

import org.junit.Test;
import uk.recurse.bitwrapper.decoder.Decoder;

import java.lang.reflect.AnnotatedElement;
import java.nio.ByteBuffer;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.recurse.bitwrapper.Wrapper.Builder.DEFAULT_DECODERS;

public class WrapperTest {

    @Test(expected = NullPointerException.class)
    public void wrapArray_nullArray_throwsException() {
        Wrapper.forView(CharSequence.class).wrap((byte[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void wrapBuffer_nullBuffer_throwsException() {
        Wrapper.forView(CharSequence.class).wrap((ByteBuffer) null);
    }

    @Test
    public void builder_addDecoder_addsToMapWithReturnTypeAsKey() {
        Decoder<?> decoder = new Decoder<Object>() {
            @Override
            public Object decode(ByteBuffer buffer, AnnotatedElement method) {
                return null;
            }
        };
        Wrapper.Builder builder = Wrapper.builder().addDecoder(decoder);
        assertTrue(builder.decoders.containsKey(Object.class));
        assertTrue(builder.decoders.containsValue(decoder));
    }

    @Test
    public void builder_buildWithNoDecoders_addsDefaultsToMap() {
        Wrapper.Builder builder = Wrapper.builder();
        assertTrue(builder.decoders.values().containsAll(DEFAULT_DECODERS));
    }

    @Test
    public void builder_buildWithNoDecoders_decodersSizeDefaultsPlus8Primitives() {
        Wrapper.Builder builder = Wrapper.builder();
        assertThat(builder.decoders.entrySet().size(), is(DEFAULT_DECODERS.size() + 8));
    }

    @Test(expected = IllegalArgumentException.class)
    public void builder_buildWithConcreteClass_throwsException() {
        Wrapper.builder().build(String.class);
    }
}
