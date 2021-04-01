package com.github.tomlj.mapper.model;

import java.util.Objects;

public class EnumColor {
  private final Color color;

  public EnumColor(Color color) {
    this.color = color;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EnumColor enumColor = (EnumColor) o;
    return color == enumColor.color;
  }

  @Override
  public int hashCode() {
    return Objects.hash(color);
  }

  @Override
  public String toString() {
    return "EnumColor{" + "color=" + color + '}';
  }
}
