package com.github.tomlj.mapper.accessor;

import com.github.tomlj.mapper.TomlObjectAccessor;
import com.github.tomlj.mapper.TomlObjectFactory;
import com.github.tomlj.mapper.TomlObjectFactoryRegistry;
import org.tomlj.TomlTable;

public final class FactoryTomlObjectAccessor<T> implements TomlObjectAccessor<T> {
  private final TomlObjectFactory<T> factory;

  public FactoryTomlObjectAccessor(TomlObjectFactory<T> factory) {
    this.factory = factory;
  }

  @Override
  public T apply(TomlObjectFactoryRegistry registry, Object value) {
    return factory.createFromTable(registry, (TomlTable) value);
  }
}
