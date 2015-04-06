package uk.recurse.bitwrapper;

import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Primitives;
import org.reflections.Reflections;
import uk.recurse.bitwrapper.decoder.Decoder;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Wrapper {

    private final Map<Class<?>, Decoder<?>> decoders;

    private Wrapper(Map<Class<?>, Decoder<?>> decoders) {
        this.decoders = decoders;
    }

    public <T> T wrap(ByteBuffer buffer, Class<T> view) {
        checkNotNull(buffer);
        checkArgument(view.isInterface(), view + " is not an interface");
        Handler handler = new Handler(new Slicer(buffer), this);
        Object proxy = Proxy.newProxyInstance(view.getClassLoader(), new Class<?>[]{view}, handler);
        return view.cast(proxy);
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

        private void addDefaultDecoders() {
            Reflections reflections = new Reflections("uk.recurse.bitwrapper.decoder");
            for (Class<? extends Decoder> decoder : reflections.getSubTypesOf(Decoder.class)) {
                try {
                    addDecoder(decoder.newInstance());
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        public Builder addDecoder(Decoder<?> decoder) {
            Class<?> returnType = getReturnType(decoder);
            decoders.put(returnType, decoder);
            decoders.put(Primitives.unwrap(returnType), decoder);
            return this;
        }

        private Class<?> getReturnType(Decoder<?> decoder) {
            try {
                Class<?>[] params = {ByteBuffer.class, Method.class, Wrapper.class};
                return decoder.getClass().getMethod("decode", params).getReturnType();
            } catch (NoSuchMethodException e) {
                throw new AssertionError("decode(...) should always exist");
            }
        }

        public Wrapper build() {
            return new Wrapper(ImmutableMap.copyOf(decoders));
        }
    }
}
