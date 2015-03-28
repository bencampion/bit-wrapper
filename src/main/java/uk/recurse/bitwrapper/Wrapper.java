package uk.recurse.bitwrapper;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Primitives;
import com.google.common.reflect.Reflection;
import uk.recurse.bitwrapper.decoder.*;

import java.lang.reflect.AnnotatedElement;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
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
        return Reflection.newProxy(view, handler);
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

        private static final List<Decoder<?>> DEFAULT_DECODERS = ImmutableList
                .<Decoder<?>>builder()
                .add(new BooleanDecoder())
                .add(new CharacterDecoder())
                .add(new ByteBufferDecoder())
                .add(new ByteDecoder())
                .add(new DoubleDecoder())
                .add(new FloatDecoder())
                .add(new InetAddressDecoder())
                .add(new IntegerDecoder())
                .add(new LongDecoder())
                .add(new ShortDecoder())
                .build();

        private final Map<Class<?>, Decoder<?>> decoders;

        private Builder() {
            decoders = new HashMap<>();
            addDecoders(DEFAULT_DECODERS);
        }

        public Builder addDecoders(Iterable<Decoder<?>> decoders) {
            for (Decoder<?> decoder : decoders) {
                addDecoder(decoder);
            }
            return this;
        }

        public Builder addDecoder(Decoder<?> decoder) {
            Class<?> returnType = getReturnType(decoder);
            decoders.put(returnType, decoder);
            decoders.put(Primitives.unwrap(returnType), decoder);
            return this;
        }

        private Class<?> getReturnType(Decoder<?> decoder) {
            try {
                Class<?>[] params = {ByteBuffer.class, AnnotatedElement.class};
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
