package uk.recurse.bitwrapper.decoder;

import org.junit.Test;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InetAddressDecoderTest {

    private final InetAddressDecoder decoder = new InetAddressDecoder();

    @Test(expected = IllegalArgumentException.class)
    public void decode_bufferSize3_throwsException() {
        decoder.decode(ByteBuffer.allocate(3), null, null);
    }

    @Test
    public void decode_bufferSize4_returnsIpv4Address() {
        byte[] bytes = {8, 8, 4, 4};
        InetAddress addr = decoder.decode(ByteBuffer.wrap(bytes), null, null);
        assertThat(addr.getHostAddress(), is("8.8.4.4"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decode_bufferSize5_throwsException() {
        decoder.decode(ByteBuffer.allocate(5), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void decode_bufferSize15_throwsException() {
        decoder.decode(ByteBuffer.allocate(15), null, null);
    }

    @Test
    public void decode_bufferSize16_returnsIpv6Address() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.asShortBuffer().put((short) 0x2001).put((short) 0x4860)
                .put((short) 0x4860).put((short) 0).put((short) 0)
                .put((short) 0).put((short) 0).put((short) 0x8844);
        InetAddress addr = decoder.decode(buffer, null, null);
        assertThat(addr.getHostAddress(), is("2001:4860:4860:0:0:0:0:8844"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decode_bufferSize17_throwsException() {
        decoder.decode(ByteBuffer.allocate(17), null, null);
    }
}