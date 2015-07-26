package uk.recurse.bitwrapper;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import uk.recurse.bitwrapper.annotation.Bits;
import uk.recurse.bitwrapper.annotation.Bytes;
import uk.recurse.bitwrapper.exception.BadExpressionException;
import uk.recurse.bitwrapper.exception.MissingAnnotationException;
import uk.recurse.bitwrapper.exception.UnsupportedTypeException;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.function.Function;

class MethodHandler {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final BufferSlicer bufferSlicer;
    private final BitWrapper wrapper;

    public MethodHandler(BufferSlicer bufferSlicer, BitWrapper wrapper) {
        this.bufferSlicer = bufferSlicer;
        this.wrapper = wrapper;
    }

    public Object invoke(Object proxy, Method method) {
        ByteBuffer slice = getSlice(proxy, method);
        Function<ByteBuffer, ?> decoder = wrapper.getDecoder(method.getReturnType());
        if (decoder != null) {
            return decoder.apply(slice);
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
        return bufferSlicer.byteSlice(offset, length);
    }

    private ByteBuffer getBitSlice(AnnotatedElement method) {
        Bits bits = method.getAnnotation(Bits.class);
        return bufferSlicer.bitSlice(bits.offset(), bits.length());
    }

    private int eval(String expression, Object root) {
        try {
            Expression exp = parser.parseExpression(expression);
            return exp.getValue(root, Integer.class);
        } catch (ExpressionException e) {
            throw new BadExpressionException(e);
        }
    }
}
