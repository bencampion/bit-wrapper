package uk.recurse.bitwrapper;

import com.google.common.reflect.AbstractInvocationHandler;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import uk.recurse.bitwrapper.annotation.Bits;
import uk.recurse.bitwrapper.annotation.Bytes;
import uk.recurse.bitwrapper.decoder.Decoder;
import uk.recurse.bitwrapper.exception.MissingAnnotationException;
import uk.recurse.bitwrapper.exception.UnsupportedTypeException;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

class Handler extends AbstractInvocationHandler {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final Slicer slicer;
    private final Wrapper wrapper;

    public Handler(Slicer slicer, Wrapper wrapper) {
        this.slicer = slicer;
        this.wrapper = wrapper;
    }

    @Override
    protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
        ByteBuffer slice = getSlice(proxy, method);
        Decoder<?> decoder = wrapper.getDecoder(method.getReturnType());
        if (decoder != null) {
            return decoder.decode(slice, method, wrapper);
        } else if (method.getReturnType().isInterface()) {
            return wrapper.wrap(slice, method.getReturnType());
        } else {
            throw new UnsupportedTypeException(method.getReturnType());
        }
    }

    private ByteBuffer getSlice(Object proxy, AnnotatedElement method) {
        if (method.isAnnotationPresent(Bytes.class)) {
            return getByteSlice(proxy, method);
        } else if (method.isAnnotationPresent(Bits.class)) {
            return getBitSlice(method);
        }
        throw new MissingAnnotationException(Bytes.class, Bits.class);
    }

    private ByteBuffer getByteSlice(Object proxy, AnnotatedElement method) {
        Bytes bytes = method.getAnnotation(Bytes.class);
        int offset = bytes.offset();
        int length = bytes.length();
        if (!bytes.offsetExp().isEmpty()) {
            offset = eval(bytes.offsetExp(), proxy);
        }
        if (!bytes.lengthExp().isEmpty()) {
            length = eval(bytes.lengthExp(), proxy);
        }
        return slicer.byteSlice(offset, length);
    }

    private ByteBuffer getBitSlice(AnnotatedElement method) {
        Bits bits = method.getAnnotation(Bits.class);
        return slicer.bitSlice(bits.offset(), bits.length());
    }

    private int eval(String expression, Object root) {
        Expression exp = parser.parseExpression(expression);
        return exp.getValue(root, Integer.class);
    }
}
