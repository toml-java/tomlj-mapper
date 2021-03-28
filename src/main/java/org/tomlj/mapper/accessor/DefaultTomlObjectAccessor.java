package org.tomlj.mapper.accessor;

import org.tomlj.mapper.TomlObjectAccessor;
import org.tomlj.mapper.TomlObjectFactoryRegistry;

public final class DefaultTomlObjectAccessor<T> implements TomlObjectAccessor<T> {
  @Override
  @SuppressWarnings("unchecked")
  public T apply(TomlObjectFactoryRegistry registry, Object value) {
    return (T) value;
  }
}
