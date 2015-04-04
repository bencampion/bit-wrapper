package uk.recurse.bitwrapper.exception;

import com.google.common.base.Joiner;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class MissingAnnotationException extends RuntimeException {

    private final Class<? extends Annotation>[] annotations;

    @SafeVarargs
    public MissingAnnotationException(Class<? extends Annotation>... annotations) {
        this.annotations = annotations;
    }

    @Override
    public String getMessage() {
        List<String> names = new ArrayList<>(annotations.length);
        for (Class<? extends Annotation> annotation : annotations) {
            names.add("@" + annotation.getSimpleName());
        }
        return Joiner.on(" or ").join(names);
    }
}
