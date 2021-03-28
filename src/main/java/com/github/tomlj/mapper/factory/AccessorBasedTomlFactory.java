package com.github.tomlj.mapper.factory;

import com.github.tomlj.mapper.TomlObjectAccessor;
import com.github.tomlj.mapper.TomlObjectFactory;
import com.github.tomlj.mapper.TomlObjectFactoryRegistry;
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
