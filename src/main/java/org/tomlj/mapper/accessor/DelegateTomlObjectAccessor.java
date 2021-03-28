package org.tomlj.mapper.accessor;

import org.tomlj.mapper.TomlObjectAccessor;
import org.tomlj.mapper.TomlObjectFactoryRegistry;

public final class DelegateTomlObjectAccessor<T> implements TomlObjectAccessor<T> {

  private TomlObjectAccessor<T> delegate;

  @Override
  public T apply(TomlObjectFactoryRegistry registry, Object value) {
    return delegate.apply(registry, value);
  }

  public void setDelegate(TomlObjectAccessor<T> delegate) {
    this.delegate = delegate;
  }
}
