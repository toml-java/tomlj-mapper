package org.tomlj.mapper.factory;

import org.tomlj.mapper.TomlObjectFactory;
import org.tomlj.mapper.TomlObjectFactoryRegistry;
import org.tomlj.TomlTable;

public final class DelegateTomlFactory<T> implements TomlObjectFactory<T> {
  private TomlObjectFactory<T> delegate;

  @Override
  public T createFromTable(TomlObjectFactoryRegistry registry, TomlTable tomlTable) {
    return delegate.createFromTable(registry, tomlTable);
  }

  public void setDelegate(TomlObjectFactory<T> delegate) {
    this.delegate = delegate;
  }
}
