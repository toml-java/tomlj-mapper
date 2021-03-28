package org.tomlj.mapper;

public interface TomlObjectAccessor<T> {
    T apply(org.tomlj.mapper.TomlObjectFactoryRegistry registry, Object value);
}
