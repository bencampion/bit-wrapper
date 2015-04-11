package uk.recurse.bitwrapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.recurse.bitwrapper.decoder.Decoder;

import java.nio.ByteBuffer;
import java.util.Map;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WrapperTest {

    @Mock
    private Map<Class<?>, Decoder<?>> decoders;

    @Mock
    private ProxyFactory proxyFactory;

    @InjectMocks
    private Wrapper wrapper;

    @Test(expected = NullPointerException.class)
    public void wrapArray_nullArray_throwsException() {
        wrapper.wrap((byte[]) null, CharSequence.class);
    }

    @Test(expected = NullPointerException.class)
    public void wrapBuffer_nullBuffer_throwsException() {
        wrapper.wrap((ByteBuffer) null, CharSequence.class);
    }

    @Test
    public void wrapBuffer_bufferAndView_invokesProxyWithView() {
        wrapper.wrap(ByteBuffer.allocate(0), CharSequence.class);
        verify(proxyFactory).create(any(MethodHandler.class), eq(CharSequence.class));
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
