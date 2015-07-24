package uk.recurse.bitwrapper.exception;

import uk.recurse.bitwrapper.annotation.Bits;
import uk.recurse.bitwrapper.annotation.Bytes;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import static java.util.stream.Collectors.joining;

/**
 * Thrown when a non-default method is invoked on view that does not have the required annotations.
 *
 * @see Bits
 * @see Bytes
 */
public class MissingAnnotationException extends RuntimeException {

    private final Class<? extends Annotation>[] annotations;

    /**
     * @param annotations the expected annotations
     */
    @SafeVarargs
    public MissingAnnotationException(Class<? extends Annotation>... annotations) {
        this.annotations = annotations;
    }

    @Override
    public String getMessage() {
        return Arrays.stream(annotations)
                .map(a -> "@" + a.getSimpleName())
                .collect(joining(" or "));
    }
}
