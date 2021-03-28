package org.tomlj.mapper.accessor;

import org.tomlj.mapper.TomlObjectAccessor;
import org.tomlj.mapper.TomlObjectFactory;
import org.tomlj.mapper.TomlObjectFactoryRegistry;
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
