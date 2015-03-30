package uk.recurse.bitwrapper.decoder;

import uk.recurse.bitwrapper.Wrapper;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public interface Decoder<T> {

    T decode(ByteBuffer buffer, Method method, Wrapper wrapper);
}
