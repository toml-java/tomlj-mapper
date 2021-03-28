package com.github.tomlj.mapper.factory;

import com.github.tomlj.mapper.TomlObjectFactory;
import com.github.tomlj.mapper.TomlObjectFactoryRegistry;
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
