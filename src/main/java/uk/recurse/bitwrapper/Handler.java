package uk.recurse.bitwrapper;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import uk.recurse.bitwrapper.annotation.Bits;
import uk.recurse.bitwrapper.annotation.Bytes;
import uk.recurse.bitwrapper.decoder.Decoder;
import uk.recurse.bitwrapper.exception.BadExpressionException;
import uk.recurse.bitwrapper.exception.MissingAnnotationException;
import uk.recurse.bitwrapper.exception.UnsupportedTypeException;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

class Handler implements InvocationHandler {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final Slicer slicer;
    private final Wrapper wrapper;

    public Handler(Slicer slicer, Wrapper wrapper) {
        this.slicer = slicer;
        this.wrapper = wrapper;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isDefault()) {
            return invokeDefault(proxy, method, args);
        }
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

    private Object invokeDefault(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> declaringClass = method.getDeclaringClass();
        Constructor<MethodHandles.Lookup> lookup = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
        lookup.setAccessible(true);
        return lookup.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
                .unreflectSpecial(method, declaringClass)
                .bindTo(proxy)
                .invokeWithArguments(args);
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
        try {
            Expression exp = parser.parseExpression(expression);
            return exp.getValue(root, Integer.class);
        } catch (ExpressionException e) {
            throw new BadExpressionException(e);
        }
    }
}
