package com.github.tomlj.mapper;

import org.tomlj.TomlTable;

public interface TomlObjectFactory<T> {
  T createFromTable(TomlObjectFactoryRegistry registry, TomlTable tomlTable);
}
