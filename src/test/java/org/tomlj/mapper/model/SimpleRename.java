package org.tomlj.mapper.model;

import org.tomlj.mapper.TomlProperty;

import java.util.List;
import java.util.Objects;

public class SimpleRename {
  private final String key1;
  private final List<Long> array1;

  public SimpleRename(@TomlProperty("key2") String key1, List<Long> array1) {
    this.key1 = key1;
    this.array1 = array1;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SimpleRename that = (SimpleRename) o;
    return Objects.equals(key1, that.key1) && Objects.equals(array1, that.array1);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key1, array1);
  }

  @Override
  public String toString() {
    return "SimpleRename{" + "key1='" + key1 + '\'' + ", array1=" + array1 + '}';
  }
}
