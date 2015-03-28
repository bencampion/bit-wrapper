package uk.recurse.bitwrapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.expression.ExpressionException;
import uk.recurse.bitwrapper.annotation.Bits;
import uk.recurse.bitwrapper.annotation.Bytes;
import uk.recurse.bitwrapper.decoder.Decoder;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HandlerTest {

    @Mock
    private TestMethods proxy;
    @Mock
    private Slicer slicer;
    @Mock
    private Wrapper wrapper;
    @Mock
    private Decoder<Integer> decoder;
    @InjectMocks
    private Handler handler;

    @Before
    public void setup() {
        when(wrapper.getDecoder(int.class)).thenReturn(decoder);
    }

    @Test
    public void handleInvocation_byteAnnotated_returnsDecodedValue() throws Throwable {
        ByteBuffer buffer = ByteBuffer.allocate(0);
        Method method = TestMethods.class.getMethod("byteAnnotated");
        when(slicer.byteSlice(7, 3)).thenReturn(buffer);
        when(decoder.decode(buffer, method)).thenReturn(42);

        Object returned = handler.handleInvocation(proxy, method, new Object[]{});

        assertThat(returned, is((Object) 42));
    }

    @Test
    public void handleInvocation_bitAnnotated_returnsDecodedValue() throws Throwable {
        ByteBuffer buffer = ByteBuffer.allocate(0);
        Method method = TestMethods.class.getMethod("bitAnnotated");
        when(slicer.bitSlice(7, 3)).thenReturn(buffer);
        when(decoder.decode(buffer, method)).thenReturn(42);

        Object returned = handler.handleInvocation(proxy, method, new Object[]{});

        assertThat(returned, is((Object) 42));
    }

    @Test
    public void handleInvocation_interfaceReturnType_returnsView() throws Throwable {
        CharSequence view = mock(CharSequence.class);
        ByteBuffer buffer = ByteBuffer.allocate(0);
        Method method = TestMethods.class.getMethod("returnsInterface");
        when(slicer.byteSlice(7, 3)).thenReturn(buffer);
        when(decoder.decode(buffer, method)).thenReturn(42);
        when(wrapper.wrap(buffer, CharSequence.class)).thenReturn(view);

        Object returned = handler.handleInvocation(view, method, new Object[]{});

        assertThat(returned, sameInstance((Object) view));
    }

    @Test
    public void handleInvocation_expressionAnnotated_returnsDecodedValue() throws Throwable {
        ByteBuffer buffer = ByteBuffer.allocate(0);
        Method method = TestMethods.class.getMethod("expressionAnnotated");
        when(proxy.offset()).thenReturn(5);
        when(proxy.length()).thenReturn(11);
        when(slicer.byteSlice(5, 11)).thenReturn(buffer);
        when(decoder.decode(buffer, method)).thenReturn(42);

        Object returned = handler.handleInvocation(proxy, method, new Object[]{});

        assertThat(returned, is((Object) 42));
    }

    @Test(expected = ExpressionException.class)
    public void handleInvocation_badSyntaxOffsetExpressions_throwsException() throws Throwable {
        Method method = TestMethods.class.getMethod("badSyntaxOffsetExpression");

        handler.handleInvocation(proxy, method, new Object[]{});
    }

    @Test(expected = ExpressionException.class)
    public void handleInvocation_badTypeOffsetExpressions_throwsException() throws Throwable {
        Method method = TestMethods.class.getMethod("badTypeOffsetExpression");

        handler.handleInvocation(proxy, method, new Object[]{});
    }

    @Test(expected = ExpressionException.class)
    public void handleInvocation_badSyntaxLengthExpressions_throwsException() throws Throwable {
        Method method = TestMethods.class.getMethod("badSyntaxLengthExpression");

        handler.handleInvocation(proxy, method, new Object[]{});
    }

    @Test(expected = ExpressionException.class)
    public void handleInvocation_badTypeLengthExpressions_throwsException() throws Throwable {
        Method method = TestMethods.class.getMethod("badTypeLengthExpression");

        handler.handleInvocation(proxy, method, new Object[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void handleInvocation_noAnnotations_throwsException() throws Throwable {
        Method method = TestMethods.class.getMethod("length");

        handler.handleInvocation(proxy, method, new Object[]{});
    }

    @SuppressWarnings("unused")
    private interface TestMethods {

        @Bytes(offset = 7, length = 3)
        int byteAnnotated();

        @Bits(offset = 7, length = 3)
        int bitAnnotated();

        @Bytes(offset = 7, length = 3)
        CharSequence returnsInterface();

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