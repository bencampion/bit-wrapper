package uk.recurse.bitwrapper;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.expression.ExpressionException;
import uk.recurse.bitwrapper.annotation.Bytes;
import uk.recurse.bitwrapper.decoder.Decoder;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HandlerTest {

    @Mock
    private TestMethods proxy;
    @Mock
    private Slicer slicer;
    @Mock
    private Decoder<Integer> decoder;

    private Handler handler;

    @Before
    public void setup() {
        handler = new Handler(slicer, ImmutableMap.<Class<?>, Decoder<?>>of(int.class, decoder));
    }

    @Test
    public void handleInvocation_byteAnnotated_returnsDecodedValue() throws Throwable {
        ByteBuffer buffer = ByteBuffer.allocate(0);
        Method method = TestMethods.class.getMethod("annotated");
        when(slicer.byteSlice(7, 3)).thenReturn(buffer);
        when(decoder.decode(buffer, method)).thenReturn(42);

        Object returned = handler.handleInvocation(proxy, method, new Object[]{});

        assertThat(returned, is((Object) 42));
    }

    @Test
    public void handleInvocation_expressionAnnotated_returnsDecodedValue() throws Throwable {
        ByteBuffer buffer = ByteBuffer.allocate(0);
        Method method = TestMethods.class.getMethod("expression");
        when(proxy.length()).thenReturn(11);
        when(slicer.byteSlice(5, 11)).thenReturn(buffer);
        when(decoder.decode(buffer, method)).thenReturn(42);

        Object returned = handler.handleInvocation(proxy, method, new Object[]{});

        assertThat(returned, is((Object) 42));
    }

    @Test(expected = ExpressionException.class)
    public void handleInvocation_badExpressions_throwsException() throws Throwable {
        Method method = TestMethods.class.getMethod("badExpression");

        handler.handleInvocation(proxy, method, new Object[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void handleInvocation_noAnnotations_throwsException() throws Throwable {
        Method method = TestMethods.class.getMethod("length");

        handler.handleInvocation(proxy, method, new Object[]{});
    }

    private interface TestMethods {
        @Bytes(offset = 7, length = 3)
        int annotated();
        @Bytes(offset = 5, lengthExp = "length()")
        int expression();
        @Bytes(offset = 5, lengthExp = "doesn't compile")
        int badExpression();
        int length();
    }
}