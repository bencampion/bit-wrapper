package uk.recurse.bitwrapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares the bits that the annotated method should return when it is invoked. Unlike {@link Bytes}, this annotation
 * will aways cause the data to be copied into a new ByteBuffer before being passed into the decoder function.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Bits {

    /**
     * The offset in bits that the annotated method returns.
     */
    int offset() default 0;

    /**
     * The number of bits the annotated method returns.
     */
    int length() default 0;
}
