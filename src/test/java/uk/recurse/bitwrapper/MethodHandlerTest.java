package uk.recurse.bitwrapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.recurse.bitwrapper.annotation.Bits;
import uk.recurse.bitwrapper.annotation.Bytes;
import uk.recurse.bitwrapper.exception.BadExpressionException;
import uk.recurse.bitwrapper.exception.MissingAnnotationException;
import uk.recurse.bitwrapper.exception.UnsupportedTypeException;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MethodHandlerTest {

    @Mock
    private TestMethods proxy;
    @Mock
    private BufferSlicer bufferSlicer;
    @Mock
    private BitWrapper wrapper;
    @Mock
    private Function<ByteBuffer, Integer> decoder;
    @InjectMocks
    private MethodHandler methodHandler;

    @Before
    public void setup() {
        when(wrapper.getDecoder(int.class)).thenReturn(decoder);
    }

    @Test
    public void invoke_byteAnnotated_returnsDecodedValue() throws NoSuchMethodException {
        ByteBuffer buffer = ByteBuffer.allocate(0);
        Method method = TestMethods.class.getMethod("byteAnnotated");
        when(bufferSlicer.byteSlice(7, 3)).thenReturn(buffer);
        when(decoder.apply(buffer)).thenReturn(42);

        Object returned = methodHandler.invoke(proxy, method);

        assertThat(returned, is((Object) 42));
    }

    @Test
    public void invoke_bitAnnotated_returnsDecodedValue() throws NoSuchMethodException {
        ByteBuffer buffer = ByteBuffer.allocate(0);
        Method method = TestMethods.class.getMethod("bitAnnotated");
        when(bufferSlicer.bitSlice(7, 3)).thenReturn(buffer);
        when(decoder.apply(buffer)).thenReturn(42);

        Object returned = methodHandler.invoke(proxy, method);

        assertThat(returned, is((Object) 42));
    }

    @Test
    public void invoke_interfaceReturnType_returnsView() throws NoSuchMethodException {
        CharSequence view = mock(CharSequence.class);
        ByteBuffer buffer = ByteBuffer.allocate(0);
        Method method = TestMethods.class.getMethod("returnsInterface");
        when(bufferSlicer.byteSlice(7, 3)).thenReturn(buffer);
        when(decoder.apply(buffer)).thenReturn(42);
        when(wrapper.wrap(buffer, CharSequence.class)).thenReturn(view);

        Object returned = methodHandler.invoke(proxy, method);

        assertThat(returned, sameInstance((Object) view));
    }

    @Test
    public void handleInvocation_expressionAnnotated_returnsDecodedValue() throws NoSuchMethodException {
        ByteBuffer buffer = ByteBuffer.allocate(0);
        Method method = TestMethods.class.getMethod("expressionAnnotated");
        when(proxy.offset()).thenReturn(5);
        when(proxy.length()).thenReturn(11);
        when(bufferSlicer.byteSlice(5, 11)).thenReturn(buffer);
        when(decoder.apply(buffer)).thenReturn(42);

        Object returned = methodHandler.invoke(proxy, method);

        assertThat(returned, is((Object) 42));
    }

    @Test(expected = BadExpressionException.class)
    public void invoke_badSyntaxOffsetExpressions_throwsException() throws NoSuchMethodException {
        Method method = TestMethods.class.getMethod("badSyntaxOffsetExpression");

        methodHandler.invoke(proxy, method);
    }

    @Test(expected = BadExpressionException.class)
    public void invoke_badTypeOffsetExpressions_throwsException() throws NoSuchMethodException {
        Method method = TestMethods.class.getMethod("badTypeOffsetExpression");

        methodHandler.invoke(proxy, method);
    }

    @Test(expected = BadExpressionException.class)
    public void invoke_badSyntaxLengthExpressions_throwsException() throws NoSuchMethodException {
        Method method = TestMethods.class.getMethod("badSyntaxLengthExpression");

        methodHandler.invoke(proxy, method);
    }

    @Test(expected = BadExpressionException.class)
    public void invoke_badTypeLengthExpressions_throwsException() throws NoSuchMethodException {
        Method method = TestMethods.class.getMethod("badTypeLengthExpression");

        methodHandler.invoke(proxy, method);
    }

    @Test(expected = MissingAnnotationException.class)
    public void invoke_noAnnotations_throwsException() throws NoSuchMethodException {
        Method method = TestMethods.class.getMethod("length");

        methodHandler.invoke(proxy, method);
    }

    @Test(expected = UnsupportedTypeException.class)
    public void invoke_unsupportedType_throwsException() throws NoSuchMethodException {
        Method method = TestMethods.class.getMethod("returnsUnsupportedType");

        methodHandler.invoke(proxy, method);
    }

    @SuppressWarnings("unused")
    public interface TestMethods {

        @Bytes(offset = 7, length = 3)
        int byteAnnotated();

        @Bits(offset = 7, length = 3)
        int bitAnnotated();

        @Bytes(offset = 7, length = 3)
        CharSequence returnsInterface();

        @Bits(offset = 5, length = 1)
        Thread returnsUnsupportedType();

        @Bytes(offsetExp = "offset()", lengthExp = "length()")
        int expressionAnnotated();

        @Bytes(offsetExp = "doesn't compile", length = 5)
        int badSyntaxOffsetExpression();

        @Bytes(offsetExp = "true", length = 5)
        int badTypeOffsetExpression();

        @Bytes(offset = 5, lengthExp = "doesn't compile")
        int badSyntaxLengthExpression();

        @Bytes(offset = 5, lengthExp = "true")
        int badTypeLengthExpression();

        int offset();

        int length();
    }
}
