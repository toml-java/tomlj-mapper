package org.tomlj.mapper.model;

import java.time.OffsetDateTime;
import java.util.Objects;

public class Owner {
  private final String name;
  private final OffsetDateTime dob;

  public Owner(String name, OffsetDateTime dob) {
    this.name = name;
    this.dob = dob;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Owner owner = (Owner) o;
    return Objects.equals(name, owner.name) && Objects.equals(dob, owner.dob);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, dob);
  }

  @Override
  public String toString() {
    return "Owner{" + "name='" + name + '\'' + ", dob=" + dob + '}';
  }
}
