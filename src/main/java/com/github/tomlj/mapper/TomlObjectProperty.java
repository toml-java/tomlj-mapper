package com.github.tomlj.mapper;

import java.util.Objects;

public final class TomlObjectProperty {
  private final String name;
  private final boolean required;

  public TomlObjectProperty(String name, boolean required) {
    this.name = name;
    this.required = required;
  }

  public String getName() {
    return name;
  }

  public boolean isRequired() {
    return required;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TomlObjectProperty that = (TomlObjectProperty) o;
    return required == that.required && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, required);
  }

  @Override
  public String toString() {
    return "TomlObjectProperty{" + "name='" + name + '\'' + ", required=" + required + '}';
  }
}
