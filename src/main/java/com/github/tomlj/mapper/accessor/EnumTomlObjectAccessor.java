package com.github.tomlj.mapper.accessor;

import com.github.tomlj.mapper.TomlObjectAccessor;
import com.github.tomlj.mapper.TomlObjectFactoryRegistry;
import com.github.tomlj.mapper.TomlObjectMapperException;
import com.github.tomlj.mapper.TomlRename;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnumTomlObjectAccessor<T> implements TomlObjectAccessor<T> {
  private final Map<String, T> enumBindings;

  public EnumTomlObjectAccessor(T[] enumConstants) {
    enumBindings =
        Arrays.stream(enumConstants).collect(Collectors.toMap(this::enumName, Function.identity()));
  }

  private String enumName(T object) {
    try {
      TomlRename tomlRename =
          object.getClass().getDeclaredField(object.toString()).getAnnotation(TomlRename.class);

      if (tomlRename != null && !"".equals(tomlRename.value())) {
        return tomlRename.value();
      }

    } catch (NoSuchFieldException e) {
      throw new TomlObjectMapperException(e);
    }

    return object.toString();
  }

  @Override
  public T apply(TomlObjectFactoryRegistry registry, Object value) {
    T result = enumBindings.get(value);

    if (result == null) {
      throw new TomlObjectMapperException("Cannot find enum value: " + value);
    }

    return result;
  }
}
