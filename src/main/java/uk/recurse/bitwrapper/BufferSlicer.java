package uk.recurse.bitwrapper;

import java.nio.ByteBuffer;

class BufferSlicer {

    private final ByteBuffer buffer;

    public BufferSlicer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public ByteBuffer byteSlice(int offset, int length) {
        checkLimit(offset, length);
        ByteBuffer copy = buffer.duplicate();
        copy.position(offset);
        copy.limit(offset + length);
        return copy.slice().asReadOnlyBuffer();
    }

    public ByteBuffer bitSlice(int offset, int length) {
        checkLimit((int) Math.ceil(offset / 8d), length / 8);
        byte[] bytes = new byte[(length + 7) / 8];
        int end = (offset + length - 1) / 8;
        int shift = (8 - (length + offset) % 8) % 8;
        for (int i = bytes.length, j = end; --i >= 0; j--) {
            bytes[i] |= (buffer.get(j) & 0xff) >>> shift;
            if (j > 0) {
                bytes[i] |= buffer.get(j - 1) << (8 - shift);
            }
        }
        if (bytes.length > 0) {
            bytes[0] &= 0xff >>> ((8 - length % 8) % 8);
        }
        return ByteBuffer.wrap(bytes).asReadOnlyBuffer();
    }

    private void checkLimit(int offset, int length) {
        if (offset > buffer.limit() || offset + length > buffer.limit()) {
            throw new IndexOutOfBoundsException();
        }
    }
}
