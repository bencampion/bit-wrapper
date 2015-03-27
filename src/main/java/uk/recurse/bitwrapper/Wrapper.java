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
import static com.google.common.base.Preconditions.checkState;

public class Wrapper<T> {

    private final Class<T> view;
    private final Map<Class<?>, Decoder<?>> decoders;

    public Wrapper(Class<T> view, Map<Class<?>, Decoder<?>> decoders) {
        this.view = view;
        this.decoders = decoders;
    }

    public T wrap(ByteBuffer buffer) {
        checkNotNull(buffer);
        Handler handler = new Handler(new Slicer(buffer), decoders);
        return Reflection.newProxy(view, handler);
    }

    public T wrap(byte[] bytes) {
        return wrap(ByteBuffer.wrap(bytes));
    }

    public static <T> Wrapper<T> forView(Class<T> view) {
        return new Builder().build(view);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        static final List<Decoder<?>> DEFAULT_DECODERS = ImmutableList
                .<Decoder<?>>builder()
                .add(new BooleanDecoder())
                .add(new ByteBufferDecoder())
                .add(new ByteDecoder())
                .add(new CharacterDecoder())
                .add(new DoubleDecoder())
                .add(new FloatDecoder())
                .add(new InetAddressDecoder())
                .add(new IntegerDecoder())
                .add(new LongDecoder())
                .add(new ShortDecoder())
                .build();

        final Map<Class<?>, Decoder<?>> decoders;

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

        public <T> Wrapper<T> build(Class<T> view) {
            checkArgument(view.isInterface(), view + " is not an interface");
            return new Wrapper<>(view, ImmutableMap.copyOf(decoders));
        }
    }
}
