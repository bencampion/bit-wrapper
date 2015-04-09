package uk.recurse.bitwrapper.exception;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import static java.util.stream.Collectors.joining;

public class MissingAnnotationException extends RuntimeException {

    private final Class<? extends Annotation>[] annotations;

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
