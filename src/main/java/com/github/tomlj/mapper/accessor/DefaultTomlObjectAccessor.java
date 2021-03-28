package com.github.tomlj.mapper.accessor;

import com.github.tomlj.mapper.TomlObjectAccessor;
import com.github.tomlj.mapper.TomlObjectFactoryRegistry;

public final class DefaultTomlObjectAccessor<T> implements TomlObjectAccessor<T> {
  @Override
  @SuppressWarnings("unchecked")
  public T apply(TomlObjectFactoryRegistry registry, Object value) {
    return (T) value;
  }
}
