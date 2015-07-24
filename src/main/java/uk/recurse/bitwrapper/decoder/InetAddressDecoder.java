package uk.recurse.bitwrapper.decoder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.function.Function;

/**
 * IP address decoder function.
 */
public class InetAddressDecoder implements Function<ByteBuffer, InetAddress> {

    /**
     * Converts a ByteBuffer into an InetAddress.
     *
     * @return an IPv4 address if the buffer contains 4 bytes or an IPv6 addres if it contains 16
     * @throws IllegalArgumentException if the buffer does not contain exactly 4 or 16 bytes
     */
    @Override
    public InetAddress apply(ByteBuffer buffer) {
        byte[] addr = new byte[buffer.limit()];
        buffer.get(addr);
        try {
            return InetAddress.getByAddress(addr);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("InetAddress length must be 4 or 16");
        }
    }
}
