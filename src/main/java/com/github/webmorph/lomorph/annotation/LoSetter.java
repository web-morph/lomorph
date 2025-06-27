package com.github.webmorph.lomorph.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class for automatic generation of a setter interface.
 * <p>
 * All non-static, non-transient, non-final, and non-ignored fields
 * (i.e. those not annotated with {@link LoIgnore}) will be included in the generated
 * interface as `setXxx(value)` method signatures.
 * <p>
 * Example:
 * <pre>{@code
 * @LoSetter
 * public class User {
 *     private String name;
 *     private final int id; // will be ignored
 * }
 * }</pre>
 * Will generate:
 * <pre>{@code
 * public interface UserSetter {
 *     void setName(String name);
 * }
 * }</pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface LoSetter {

}
