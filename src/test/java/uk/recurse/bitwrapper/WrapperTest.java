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
        Decoder<?> decoder = new Decoder<String>() {
            @Override
            public String decode(ByteBuffer buffer, AnnotatedElement method) {
                return null;
            }
        };
        Wrapper.Builder builder = Wrapper.builder().addDecoder(decoder);
        assertThat(builder.decoders.entrySet().size(), is(1));
        assertTrue(builder.decoders.containsKey(String.class));
        assertTrue(builder.decoders.containsValue(decoder));
    }

    @Test
    public void builder_addPrimitiveDecoder_addsToMapWithPrimitiveAndObjectReturnTypeAsKey() {
        Decoder<?> decoder = new Decoder<Integer>() {
            @Override
            public Integer decode(ByteBuffer buffer, AnnotatedElement method) {
                return 0;
            }
        };
        Wrapper.Builder builder = Wrapper.builder().addDecoder(decoder);
        assertThat(builder.decoders.entrySet().size(), is(2));
        assertTrue(builder.decoders.containsKey(Integer.class));
        assertTrue(builder.decoders.containsKey(int.class));
        assertTrue(builder.decoders.containsValue(decoder));
    }

    @Test
    public void builder_addDefaultDecoders_addsDefaultsToMap() {
        Wrapper.Builder builder = Wrapper.builder().addDefaultDecoders();
        assertTrue(builder.decoders.values().containsAll(DEFAULT_DECODERS));
    }

    @Test(expected = IllegalStateException.class)
    public void builder_buildWithNoDecoders_throwsException() {
        Wrapper.builder().build(Iterable.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builder_buildWithConcreteClass_throwsException() {
        Wrapper.builder().addDefaultDecoders().build(String.class);
    }
}

