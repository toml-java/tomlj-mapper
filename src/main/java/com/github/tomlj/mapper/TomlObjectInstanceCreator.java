package com.github.tomlj.mapper;

@FunctionalInterface
public interface TomlObjectInstanceCreator<T> {
  T create();
}
