package com.github.tomlj.mapper.model;

import com.github.tomlj.mapper.TomlProperty;
import java.util.List;

public class RequiredField {
  private final String key1;
  private final String key2;
  private final List<Long> array1;

  public RequiredField(String key1, @TomlProperty(required = true) String key2, List<Long> array1) {
    this.key1 = key1;
    this.key2 = key2;
    this.array1 = array1;
  }
}
