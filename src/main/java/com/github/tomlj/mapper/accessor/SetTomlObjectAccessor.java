package com.github.tomlj.mapper.accessor;

import com.github.tomlj.mapper.TomlObjectAccessor;
import com.github.tomlj.mapper.TomlObjectFactoryRegistry;
import com.github.tomlj.mapper.TomlObjectInstanceCreator;
import java.util.Set;
import java.util.stream.Collector;
import org.tomlj.TomlArray;

public class SetTomlObjectAccessor<T> implements TomlObjectAccessor<Set<T>> {
  private final TomlObjectInstanceCreator<? extends Set<T>> instanceCreator;
  private final TomlObjectAccessor<T> itemsAccessor;

  public SetTomlObjectAccessor(
      TomlObjectInstanceCreator<? extends Set<T>> instanceCreator,
      TomlObjectAccessor<T> itemsAccessor) {
    this.instanceCreator = instanceCreator;
    this.itemsAccessor = itemsAccessor;
  }

  @Override
  public Set<T> apply(TomlObjectFactoryRegistry registry, Object value) {
    TomlArray tomlArray = (TomlArray) value;
    return tomlArray.toList().stream()
        .map(element -> itemsAccessor.apply(registry, element))
        .collect(
            Collector.of(
                instanceCreator::create,
                Set::add,
                (left, right) -> {
                  left.addAll(right);
                  return left;
                }));
  }
}
