package com.github.tomlj.mapper.model;

import java.util.List;
import java.util.Objects;

public class Simple {
  private final String key1;
  private final List<Long> array1;

  public Simple(String key1, List<Long> array1) {
    this.key1 = key1;
    this.array1 = array1;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Simple simple = (Simple) o;
    return Objects.equals(key1, simple.key1) && Objects.equals(array1, simple.array1);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key1, array1);
  }

  @Override
  public String toString() {
    return "Simple{" + "key1='" + key1 + '\'' + ", array1=" + array1 + '}';
  }
}
