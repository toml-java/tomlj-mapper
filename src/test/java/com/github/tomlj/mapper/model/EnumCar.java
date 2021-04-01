package com.github.tomlj.mapper.model;

import java.util.Objects;

public class EnumCar {
  private final Car car1;
  private final Car car2;
  private final Car car3;

  public EnumCar(Car car1, Car car2, Car car3) {
    this.car1 = car1;
    this.car2 = car2;
    this.car3 = car3;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EnumCar enumCar = (EnumCar) o;
    return car1 == enumCar.car1 && car2 == enumCar.car2 && car3 == enumCar.car3;
  }

  @Override
  public int hashCode() {
    return Objects.hash(car1, car2, car3);
  }

  @Override
  public String toString() {
    return "EnumCar{" + "car1=" + car1 + ", car2=" + car2 + ", car3=" + car3 + '}';
  }
}
