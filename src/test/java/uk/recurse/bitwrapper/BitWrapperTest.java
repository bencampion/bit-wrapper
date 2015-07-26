package uk.recurse.bitwrapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BitWrapperTest {

    @Mock
    private Map<Class<?>, Function<ByteBuffer, ?>> decoders;

    @Mock
    private ProxyFactory proxyFactory;

    @InjectMocks
    private BitWrapper wrapper;

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
        Function<ByteBuffer, Object> decoder = (buffer) -> new Object();
        BitWrapper wrapper = BitWrapper.builder().addDecoder(Object.class, decoder).build();
        assertThat(wrapper.getDecoder(Object.class), sameInstance(decoder));
    }

    @Test
    public void builder_addPrimitiveDecoder_addsDecoderToWrapperForPrimitiveAndWrappedTypes() {
        Function<ByteBuffer, Integer> decoder = (buffer) -> 0;
        BitWrapper wrapper = BitWrapper.builder().addDecoder(Integer.class, decoder).build();
        assertThat(wrapper.getDecoder(Integer.class), sameInstance(decoder));
        assertThat(wrapper.getDecoder(int.class), sameInstance(decoder));
    }
}
