package uk.recurse.bitwrapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares the bytes that the annotated method should return when it is invoked. The ByteBuffer passed into the decoder
 * function will be a view of the underlying data in order to avoid unnecessary data copying. Offset and length
 * expressions take precedence over literal values when both are declared.
 *
 * @see Bits
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Bytes {

    /**
     * The offset in bytes that the annotated method returns.
     */
    int offset() default 0;

    /**
     * An expression to be evaluated that returns the offset. Expressions can refer other methods on the class and can
     * use standard boolean and arithmetic operators, for example: {@code crc() == 1 ? length() + 4 : length()}.
     *
     * @see #offset()
     */
    String offsetExp() default "";

    /**
     * The number of bytes the annotated method returns.
     */
    int length() default 0;

    /**
     * An expression to be evaluated the returns the length. See {@link #offsetExp()} for more information about
     * expressions.
     *
     * @see #length()
     */
    String lengthExp() default "";
}
