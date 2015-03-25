package uk.recurse.bitwrapper;

import com.google.common.reflect.AbstractInvocationHandler;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import uk.recurse.bitwrapper.annotation.Bytes;
import uk.recurse.bitwrapper.decoder.Decoder;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

class Handler extends AbstractInvocationHandler {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final Slicer slicer;
    private final Map<Class<?>, Decoder<?>> decoders;

    public Handler(Slicer slicer, Map<Class<?>, Decoder<?>> decoders) {
        this.slicer = slicer;
        this.decoders = decoders;
    }

    @Override
    protected Object handleInvocation(Object proxy, Method method, Object[] args)
            throws Throwable {
        ByteBuffer range = getSlice(proxy, method);
        Decoder<?> decoder = decoders.get(method.getReturnType());
        return decoder.decode(range, method);
    }

    private ByteBuffer getSlice(Object proxy, AnnotatedElement method) {
        checkArgument(method.isAnnotationPresent(Bytes.class));
        Bytes bytes = method.getAnnotation(Bytes.class);
        int length = bytes.length();
        if (!bytes.lengthExp().isEmpty()) {
            Expression exp = parser.parseExpression(bytes.lengthExp());
            length = exp.getValue(proxy, Integer.class);
        }
        return slicer.byteSlice(bytes.offset(), length);
    }
}
