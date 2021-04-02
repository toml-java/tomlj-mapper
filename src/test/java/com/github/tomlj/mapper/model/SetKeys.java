package com.github.tomlj.mapper.model;

import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;

public class SetKeys {
  private final Set<String> data;
  private final SortedSet<String> sorted;
  private final NavigableSet<String> navigable;

  public SetKeys(Set<String> data, SortedSet<String> sorted, NavigableSet<String> navigable) {
    this.data = data;
    this.sorted = sorted;
    this.navigable = navigable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SetKeys setKeys = (SetKeys) o;
    return Objects.equals(data, setKeys.data)
        && Objects.equals(sorted, setKeys.sorted)
        && Objects.equals(navigable, setKeys.navigable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, sorted, navigable);
  }

  @Override
  public String toString() {
    return "SetKeys{" + "data=" + data + ", sorted=" + sorted + ", navigable=" + navigable + '}';
  }
}
