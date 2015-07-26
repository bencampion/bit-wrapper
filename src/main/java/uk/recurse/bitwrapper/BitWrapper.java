package uk.recurse.bitwrapper;

import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Primitives;
import org.reflections.Reflections;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Wraps ByteBuffers and byte arrays with interfaces (referred to as <i>views</i>) which have annotated methods that
 * describe slices of the buffer and the data type of each slice. Implementations of the view interfaces are generated
 * dynamically at runtime and the slice described by each method is not extracted from the underlying buffer until the
 * method is invoked. Invocations are not cached so any changes made to the buffer after wrapping will be visible in the
 * view.
 * <p>
 * Example view:
 * <pre><code>
 * interface UdpPacket {
 *    {@literal @}Bytes(length = 2)
 *     int sourcePort();
 *    {@literal @}Bytes(offset = 2, length = 2)
 *     int destinationPort();
 *    {@literal @}Bytes(offset = 4, length = 2)
 *     int length();
 *    {@literal @}Bytes(offset = 6, length = 2)
 *     short checksum();
 *    {@literal @}Bytes(offset = 8, lengthExp = "length() - 8")
 *     ByteBuffer payload();
 * }
 * </code></pre>
 * Using the view:
 * <pre><code>
 * BitWrapper wrapper = BitWrapper.create();
 * UdpPacket udpPacket = wrapper.wrap(buffer, UdpPacket.class);
 * int source = udpPacket.sourcePort();
 * </code></pre>
 */
public class BitWrapper {

    private final Map<Class<?>, Function<ByteBuffer, ?>> decoders;
    private final ProxyFactory proxyFactory;

    private BitWrapper(Map<Class<?>, Function<ByteBuffer, ?>> decoders, ProxyFactory proxyFactory) {
        this.decoders = decoders;
        this.proxyFactory = proxyFactory;
    }

    /**
     * Returns an instance of a view backed by an underlying ByteBuffer. The portion of the buffer that the view will
     * wrap is defined by its position and limit at the time it was wrapped. Modifications made to the buffer data after
     * wrapping will be visible from the view, but changes made to mark, position and limit are not.
     *
     * @param buffer the buffer to wrap
     * @param view   the view to use
     * @return an instance of the view that wraps the ByteBuffer
     */
    public <T> T wrap(ByteBuffer buffer, Class<T> view) {
        MethodHandler methodHandler = new MethodHandler(new BufferSlicer(buffer.slice()), this);
        return proxyFactory.create(methodHandler, view);
    }

    /**
     * Returns an instance of a view backed by an underlying byte array. Modifications made to the array data after
     * wrapping will be visible from the view.
     *
     * @param bytes the bytes to wrap
     * @param view  the view to use
     * @return an instance of the view that wraps the byte array
     */
    public <T> T wrap(byte[] bytes, Class<T> view) {
        return wrap(ByteBuffer.wrap(bytes), view);
    }

    @SuppressWarnings("unchecked")
    <T> Function<ByteBuffer, T> getDecoder(Class<T> type) {
        return (Function<ByteBuffer, T>) decoders.get(type);
    }

    /**
     * Returns a new instance that uses the decoders in the {@link uk.recurse.bitwrapper.decoder} package.
     */
    public static BitWrapper create() {
        return new Builder().build();
    }

    /**
     * Returns a new builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for creating BitWrapper instances. New builders are created using {@link BitWrapper#builder()}.
     */
    public static class Builder {

        private final Map<Class<?>, Function<ByteBuffer, ?>> decoders;

        private Builder() {
            decoders = new HashMap<>();
            addDefaultDecoders();
        }

        @SuppressWarnings("unchecked")
        private void addDefaultDecoders() {
            Reflections reflections = new Reflections("uk.recurse.bitwrapper.decoder");
            reflections.getSubTypesOf(Function.class).forEach(decoder -> {
                try {
                    Class<?>[] params = {ByteBuffer.class};
                    Class<?> type = decoder.getMethod("apply", params).getReturnType();
                    addDecoder(type, decoder.newInstance());
                } catch (ReflectiveOperationException e) {
                    throw new AssertionError(e);
                }
            });
        }

        /**
         * Adds an additional decoder function.
         *
         * @param type    the Java type decoded by this decoder
         * @param decoder the decoder
         */
        public <T> Builder addDecoder(Class<T> type, Function<ByteBuffer, T> decoder) {
            decoders.put(type, decoder);
            decoders.put(Primitives.unwrap(type), decoder);
            return this;
        }

        /**
         * Returns a newly created BitWrapper
         */
        public BitWrapper build() {
            return new BitWrapper(ImmutableMap.copyOf(decoders), new ProxyFactory());
        }
    }
}
