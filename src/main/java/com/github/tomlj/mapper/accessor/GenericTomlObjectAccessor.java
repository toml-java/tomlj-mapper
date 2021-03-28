package com.github.tomlj.mapper.accessor;

import com.github.tomlj.mapper.TomlObjectAccessor;
import com.github.tomlj.mapper.TomlObjectFactoryRegistry;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.tomlj.TomlArray;
import org.tomlj.TomlTable;

public final class GenericTomlObjectAccessor implements TomlObjectAccessor<Object> {
  @Override
  public Object apply(TomlObjectFactoryRegistry registry, Object value) {
    return toObject(value);
  }

  private static Object toObject(Object value) {
    if (value instanceof TomlArray) {
      return toList((TomlArray) value);
    } else if (value instanceof TomlTable) {
      return toMap((TomlTable) value);
    }
    return value;
  }

  private static Object toMap(TomlTable tomlTable) {
    return tomlTable.keySet().stream()
        .collect(Collectors.toMap(Function.identity(), key -> toObject(tomlTable.get(key))));
  }

  private static Object toList(TomlArray tomlArray) {
    return tomlArray.toList().stream()
        .map(GenericTomlObjectAccessor::toObject)
        .collect(Collectors.toList());
  }
}
