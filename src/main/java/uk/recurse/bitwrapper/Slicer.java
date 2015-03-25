package uk.recurse.bitwrapper;

import java.nio.ByteBuffer;

class Slicer {

    private final ByteBuffer buffer;

    public Slicer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public ByteBuffer byteSlice(int offset, int length) {
        ByteBuffer copy = buffer.duplicate();
        copy.position(offset);
        ByteBuffer slice = copy.slice();
        slice.limit(length);
        return slice;
    }
}
