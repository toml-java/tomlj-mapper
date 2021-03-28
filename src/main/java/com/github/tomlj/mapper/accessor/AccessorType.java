package com.github.tomlj.mapper.accessor;

import java.lang.reflect.Type;
import java.util.Objects;

public final class AccessorType {
  private final Class<?> aClass;
  private final Type type;

  public AccessorType(Class<?> aClass, Type type) {
    this.aClass = aClass;
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AccessorType that = (AccessorType) o;
    return Objects.equals(aClass, that.aClass) && Objects.equals(type, that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aClass, type);
  }

  @Override
  public String toString() {
    return "AccessorType{" + "aClass=" + aClass + ", type=" + type + '}';
  }
}
