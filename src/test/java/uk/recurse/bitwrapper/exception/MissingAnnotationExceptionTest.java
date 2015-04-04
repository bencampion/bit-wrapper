package uk.recurse.bitwrapper.exception;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MissingAnnotationExceptionTest {

    @Test
    public void getMessage_noAnnotation_returnsEmptyString() {
        String message = new MissingAnnotationException().getMessage();
        assertThat(message, is(""));
    }

    @Test
    public void getMessage_oneAnnotation_returnsAnnotationName() {
        String message = new MissingAnnotationException(Override.class).getMessage();
        assertThat(message, is("@Override"));
    }

    @Test
    public void getMessage_twoAnnotations_returnsAnnotationNames() {
        String message = new MissingAnnotationException(Override.class, Deprecated.class).getMessage();
        assertThat(message, is("@Override or @Deprecated"));
    }
}