package com.github.tomlj.mapper.accessor;

import com.github.tomlj.mapper.TomlObjectAccessor;
import com.github.tomlj.mapper.TomlObjectFactoryRegistry;
import java.util.List;
import java.util.stream.Collectors;
import org.tomlj.TomlArray;

public final class ListTomlObjectAccessor<T> implements TomlObjectAccessor<List<T>> {
  private final TomlObjectAccessor<T> itemsAccessor;

  public ListTomlObjectAccessor(TomlObjectAccessor<T> itemsAccessor) {
    this.itemsAccessor = itemsAccessor;
  }

  @Override
  public List<T> apply(TomlObjectFactoryRegistry registry, Object value) {
    TomlArray tomlArray = (TomlArray) value;
    return tomlArray.toList().stream()
        .map(element -> itemsAccessor.apply(registry, element))
        .collect(Collectors.toList());
  }
}
