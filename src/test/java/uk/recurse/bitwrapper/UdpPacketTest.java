package uk.recurse.bitwrapper;

import org.junit.Before;
import org.junit.Test;
import uk.recurse.bitwrapper.annotation.Bytes;

import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UdpPacketTest {

    private byte[] packet;
    private UdpPacket udp;

    @Before
    public void setup() {
        packet = new byte[]{0, 42, 0, 94, 0, 9, 1, 0, -1};
        udp = BitWrapper.create().wrap(packet, UdpPacket.class);
    }

    @Test
    public void sourcePort() {
        assertThat(udp.sourcePort(), is(42));
    }

    @Test
    public void destinationPort() {
        assertThat(udp.destinationPort(), is(94));
    }

    @Test
    public void length() {
        assertThat(udp.length(), is(9));
    }

    @Test
    public void checksum() {
        assertThat(udp.checksum(), is((short) 256));
    }

    @Test
    public void info() {
        assertThat(udp.info(), is("src:42 dest:94"));
    }

    @Test
    public void payload() {
        ByteBuffer payload = ByteBuffer.wrap(new byte[]{-1});
        assertThat(udp.payload(), is(payload));
    }

    @Test(expected = ReadOnlyBufferException.class)
    public void payload_modifyingBuffer_throwsException() {
        udp.payload().put(0, (byte) 42);
    }

    @Test
    public void payload_modifyingWrappedArray_modifiesBuffer() {
        packet[8] = 42;
        assertThat(udp.payload().get(0), is((byte) 42));
    }

    public interface UdpPacket {

        @Bytes(offset = 0, length = 2)
        int sourcePort();

        @Bytes(offset = 2, length = 2)
        int destinationPort();

        @Bytes(offset = 4, length = 2)
        int length();

        @Bytes(offset = 6, length = 2)
        short checksum();

        @Bytes(offset = 8, lengthExp = "length() - 8")
        ByteBuffer payload();

        default String info() {
            return String.format("src:%d dest:%d", sourcePort(), destinationPort());
        }
    }
}
