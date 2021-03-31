package com.github.tomlj.mapper;

@FunctionalInterface
public interface TomlObjectInstanceCreatorFactory {
  <T> TomlObjectInstanceCreator<T> forClass(Class<T> aClass);
}
