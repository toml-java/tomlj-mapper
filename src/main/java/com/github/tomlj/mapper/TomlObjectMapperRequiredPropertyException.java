package com.github.tomlj.mapper;

public class TomlObjectMapperRequiredPropertyException extends TomlObjectMapperException {
  private final String property;

  public TomlObjectMapperRequiredPropertyException(String property) {
    super("Required property is missing: " + property);
    this.property = property;
  }

  public String getProperty() {
    return property;
  }
}
