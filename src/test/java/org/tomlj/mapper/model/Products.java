package org.tomlj.mapper.model;

import java.util.Objects;

public class Products {
  private final String name;
  private final Long sku;
  private final String color;

  public Products(String name, Long sku, String color) {
    this.name = name;
    this.sku = sku;
    this.color = color;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Products products = (Products) o;
    return Objects.equals(name, products.name)
        && Objects.equals(sku, products.sku)
        && Objects.equals(color, products.color);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, sku, color);
  }

  @Override
  public String toString() {
    return "Products{" + "name='" + name + '\'' + ", sku=" + sku + ", color='" + color + '\'' + '}';
  }
}
