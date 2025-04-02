package io.github.killiansra.javacman.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated element is intended for debugging purposes only.
 */
@Target(ElementType.METHOD)
public @interface DebugOnly
{
    String value() default "This method is for debugging only.";
}
