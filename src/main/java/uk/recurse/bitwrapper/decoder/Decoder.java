package uk.recurse.bitwrapper.decoder;

import java.lang.reflect.AnnotatedElement;
import java.nio.ByteBuffer;

public interface Decoder<T> {

    T decode(ByteBuffer buffer, AnnotatedElement method);
}
