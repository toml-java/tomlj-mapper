package com.github.tomlj.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({
  ElementType.PARAMETER,
  ElementType.TYPE_PARAMETER,
})
public @interface TomlProperty {
  String value() default "";

  boolean required() default false;
}
