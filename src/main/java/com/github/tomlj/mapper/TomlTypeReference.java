package com.github.tomlj.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TomlTypeReference<T> {
  private final Type type;
  private Constructor<T> constructor;

  protected TomlTypeReference() {
    Type superclass = getClass().getGenericSuperclass();
    if (superclass instanceof Class) {
      throw new TomlObjectMapperException("Missing type parameter.");
    }
    this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
  }

  public T newInstance()
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
          InstantiationException {
    if (constructor == null) {
      @SuppressWarnings("unchecked")
      Class<T> rawType =
          type instanceof Class<?>
              ? (Class<T>) type
              : (Class<T>) ((ParameterizedType) type).getRawType();
      constructor = rawType.getConstructor();
    }
    return constructor.newInstance();
  }

  public Type getType() {
    return this.type;
  }
}
