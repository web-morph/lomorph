package com.github.webmorph.lomorph.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class for automatic generation of a getter interface.
 * <p>
 * All non-static, non-transient, and non-ignored fields ({@link LoIgnore})
 * will be included in the generated interface with corresponding `getXxx()` methods.
 * <p>
 * Example:
 * <pre>{@code
 * @LoGetter
 * public class User {
 *     private String name;
 * }
 * }</pre>
 * Will generate:
 * <pre>{@code
 * public interface UserGetter {
 *     String getName();
 * }
 * }</pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface LoGetter {

}
