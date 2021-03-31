package com.github.tomlj.mapper.model;

import com.github.tomlj.mapper.TomlProperty;
import java.util.LinkedList;
import java.util.Objects;
import java.util.TreeMap;

public class CustomTypes {
  private final LinkedList<String> linkedList;
  private final TreeMap<String, String> treeMap;

  public CustomTypes(
      @TomlProperty("linked_list") LinkedList<String> linkedList,
      @TomlProperty("tree_map") TreeMap<String, String> treeMap) {
    this.linkedList = linkedList;
    this.treeMap = treeMap;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CustomTypes that = (CustomTypes) o;
    return Objects.equals(linkedList, that.linkedList) && Objects.equals(treeMap, that.treeMap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(linkedList, treeMap);
  }

  @Override
  public String toString() {
    return "CustomTypes{" + "linkedList=" + linkedList + ", treeMap=" + treeMap + '}';
  }
}
