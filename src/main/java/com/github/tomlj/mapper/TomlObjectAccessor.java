package com.github.tomlj.mapper;

public interface TomlObjectAccessor<T> {
  T apply(TomlObjectFactoryRegistry registry, Object value);
}
