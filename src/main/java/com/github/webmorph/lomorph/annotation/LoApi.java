package com.github.webmorph.lomorph.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Shortcut annotation to generate both getter and setter interfaces for a class.
 * <p>
 * When a class is annotated with {@code @LoApi}, the {@code [ClassName]Api} interface
 * will be generated at compile-time, containing method signatures for:
 * <ul>
 *     <li>All public-style getters: {@code getFieldName()}</li>
 *     <li>All setters for non-final fields: {@code setFieldName(...)}</li>
 * </ul>
 *
 * <p>Fields marked with {@link LoIgnore} will be excluded.</p>
 *
 * Example:
 * <pre>{@code
 * @LoApi
 * public class User {
 *     private final int id;
 *     private String name;
 * }
 * }</pre>
 *
 * <p>Generates:</p>
 * <pre>{@code
 * public interface UserApi {
 *     int getId();
 *     String getName();
 *     void setName(String name);
 * }
 * }</pre>
 *
 * @see LoGetter
 * @see LoSetter
 * @see LoIgnore
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface LoApi {

}
