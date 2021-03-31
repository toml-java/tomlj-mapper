package com.github.tomlj.mapper.accessor;

import com.github.tomlj.mapper.TomlObjectAccessor;
import com.github.tomlj.mapper.TomlObjectFactoryRegistry;
import com.github.tomlj.mapper.TomlObjectInstanceCreator;
import java.util.List;
import java.util.stream.Collector;
import org.tomlj.TomlArray;

public final class ListTomlObjectAccessor<T> implements TomlObjectAccessor<List<T>> {
  private final TomlObjectAccessor<T> itemsAccessor;
  private final TomlObjectInstanceCreator<? extends List<T>> instanceCreator;

  public ListTomlObjectAccessor(
      TomlObjectInstanceCreator<? extends List<T>> instanceCreator,
      TomlObjectAccessor<T> itemsAccessor) {
    this.itemsAccessor = itemsAccessor;
    this.instanceCreator = instanceCreator;
  }

  @Override
  public List<T> apply(TomlObjectFactoryRegistry registry, Object value) {
    TomlArray tomlArray = (TomlArray) value;
    return tomlArray.toList().stream()
        .map(element -> itemsAccessor.apply(registry, element))
        .collect(
            Collector.of(
                instanceCreator::create,
                List::add,
                (left, right) -> {
                  left.addAll(right);
                  return left;
                }));
  }
}
