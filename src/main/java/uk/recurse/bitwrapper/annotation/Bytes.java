package uk.recurse.bitwrapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Bytes {

    int offset() default 0;

    String offsetExp() default "";

    int length() default 0;

    String lengthExp() default "";
}
