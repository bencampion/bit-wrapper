package uk.recurse.bitwrapper;

import org.junit.Before;
import org.junit.Test;
import uk.recurse.bitwrapper.annotation.Bits;
import uk.recurse.bitwrapper.annotation.Bytes;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Ipv6PacketTest {

    private static final String SOURCE = "2001:4860:4860:0:0:0:0:8888";
    private static final String DESTINATION = "2001:4860:4860:0:0:0:0:8844";

    private Ipv6Packet packet;

    @Before
    public void setup() throws UnknownHostException {
        ByteBuffer buffer = ByteBuffer.allocate(40);
        buffer.putInt(0b01101000_01111000_00000000_00000001);
        buffer.putInt(0b00000000_00000001_10000001_10000001);
        buffer.put(InetAddress.getByName(SOURCE).getAddress());
        buffer.put(InetAddress.getByName(DESTINATION).getAddress());
        buffer.rewind();
        packet = BitWrapper.create().wrap(buffer, Ipv6Packet.class);
    }

    @Test
    public void version() {
        assertThat(packet.version(), is((byte) 6));
    }

    @Test
    public void diffServe() {
        assertThat(packet.trafficClass().diffServe(), is((byte) 33));
    }

    @Test
    public void ecn() {
        assertThat(packet.trafficClass().ecn(), is((byte) 3));
    }

    @Test
    public void flowLabel() {
        assertThat(packet.flowLabel(), is(524289));
    }

    @Test
    public void payloadLength() {
        assertThat(packet.payloadLength(), is(1));
    }

    @Test
    public void nextHeader() {
        assertThat(packet.nextHeader(), is((byte) -127));
    }

    @Test
    public void hopLimit() {
        assertThat(packet.hopLimit(), is((short) 129));
    }

    @Test
    public void sourceAddress() {
        assertThat(packet.sourceAddress().getHostAddress(), is(SOURCE));
    }

    @Test
    public void destinationAddress() {
        assertThat(packet.destinationAddress().getHostAddress(), is(DESTINATION));
    }

    public interface Ipv6Packet {

        @Bits(length = 4)
        byte version();

        @Bits(offset = 4, length = 8)
        TrafficClass trafficClass();

        @Bits(offset = 12, length = 20)
        int flowLabel();

        @Bytes(offset = 4, length = 2)
        int payloadLength();

        @Bytes(offset = 6, length = 1)
        byte nextHeader();

        @Bytes(offset = 7, length = 1)
        short hopLimit();

        @Bytes(offset = 8, length = 16)
        InetAddress sourceAddress();

        @Bytes(offset = 24, length = 16)
        InetAddress destinationAddress();

        @Bytes(offset = 40, lengthExp = "payloadLength()")
        ByteBuffer payload();
    }

    public interface TrafficClass {

        @Bits(length = 6)
        byte diffServe();

        @Bits(offset = 6, length = 2)
        byte ecn();
    }


}
