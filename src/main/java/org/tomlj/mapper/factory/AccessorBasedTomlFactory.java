package org.tomlj.mapper.factory;

import org.tomlj.mapper.TomlObjectAccessor;
import org.tomlj.mapper.TomlObjectFactory;
import org.tomlj.mapper.TomlObjectFactoryRegistry;
import org.tomlj.TomlTable;

public final class AccessorBasedTomlFactory<T> implements TomlObjectFactory<T> {
  private final TomlObjectAccessor<T> accessor;

  public AccessorBasedTomlFactory(TomlObjectAccessor<T> accessor) {
    this.accessor = accessor;
  }

  @Override
  public T createFromTable(TomlObjectFactoryRegistry registry, TomlTable tomlTable) {
    return accessor.apply(registry, tomlTable);
  }
}
