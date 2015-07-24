package uk.recurse.bitwrapper.decoder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.function.Function;

public class InetAddressDecoder implements Function<ByteBuffer, InetAddress> {

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
