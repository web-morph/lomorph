package com.github.webmorph.lomorph.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field to be ignored during code generation by {@link LoGetter} and {@link LoSetter}.
 * <p>
 * This field will not appear in generated getter/setter interfaces.
 * <p>
 * Example:
 * <pre>{@code
 * public class User {
 *     @LoIgnore
 *     private String internalNote;
 * }
 * }</pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface LoIgnore {
}
