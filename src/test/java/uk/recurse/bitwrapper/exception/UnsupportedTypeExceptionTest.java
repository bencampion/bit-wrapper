package uk.recurse.bitwrapper.exception;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UnsupportedTypeExceptionTest {

    @Test
    public void getMessage_oneAnnotation_returnsAnnotationName() {
        String message = new UnsupportedTypeException(String.class).getMessage();
        assertThat(message, is("class java.lang.String is not a supported type"));
    }
}