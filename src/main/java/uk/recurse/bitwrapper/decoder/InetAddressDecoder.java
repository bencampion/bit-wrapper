package uk.recurse.bitwrapper.decoder;

import uk.recurse.bitwrapper.Wrapper;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class InetAddressDecoder implements Decoder<InetAddress> {

    @Override
    public InetAddress decode(ByteBuffer buffer, Method method, Wrapper wrapper) {
        byte[] addr = new byte[buffer.limit()];
        buffer.get(addr);
        try {
            return InetAddress.getByAddress(addr);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("InetAddress length must be 4 or 16");
        }
    }
}
