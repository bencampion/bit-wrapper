package uk.recurse.bitwrapper;

import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Primitives;
import org.reflections.Reflections;
import uk.recurse.bitwrapper.decoder.Decoder;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Wrapper {

    private final Map<Class<?>, Decoder<?>> decoders;
    private final ProxyFactory proxyFactory;

    private Wrapper(Map<Class<?>, Decoder<?>> decoders, ProxyFactory proxyFactory) {
        this.decoders = decoders;
        this.proxyFactory = proxyFactory;
    }

    public <T> T wrap(ByteBuffer buffer, Class<T> view) {
        MethodHandler methodHandler = new MethodHandler(new Slicer(buffer.slice()), this);
        return proxyFactory.create(methodHandler, view);
    }

    public <T> T wrap(byte[] bytes, Class<T> view) {
        return wrap(ByteBuffer.wrap(bytes), view);
    }

    @SuppressWarnings("unchecked")
    <T> Decoder<T> getDecoder(Class<T> type) {
        return (Decoder<T>) decoders.get(type);
    }

    public static Wrapper create() {
        return new Builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Map<Class<?>, Decoder<?>> decoders;

        private Builder() {
            decoders = new HashMap<>();
            addDefaultDecoders();
        }

        @SuppressWarnings("unchecked")
        private void addDefaultDecoders() {
            Reflections reflections = new Reflections("uk.recurse.bitwrapper.decoder");
            reflections.getSubTypesOf(Decoder.class).forEach(decoder -> {
                try {
                    Class<?>[] params = {ByteBuffer.class, Method.class, Wrapper.class};
                    Class<?> type = decoder.getMethod("decode", params).getReturnType();
                    addDecoder(type, decoder.newInstance());
                } catch (ReflectiveOperationException e) {
                    throw new AssertionError(e);
                }
            });
        }

        public <T> Builder addDecoder(Class<T> type, Decoder<T> decoder) {
            decoders.put(type, decoder);
            decoders.put(Primitives.unwrap(type), decoder);
            return this;
        }

        public Wrapper build() {
            return new Wrapper(ImmutableMap.copyOf(decoders), new ProxyFactory());
        }
    }
}
