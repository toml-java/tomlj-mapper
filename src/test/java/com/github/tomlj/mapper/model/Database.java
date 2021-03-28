package com.github.tomlj.mapper.model;

import com.github.tomlj.mapper.TomlProperty;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Database {
  private final boolean enabled;
  private final List<Long> ports;
  private final Object data;
  private final Map<String, Double> tempTargets;

  public Database(
      boolean enabled,
      List<Long> ports,
      Object data,
      @TomlProperty("temp_targets") Map<String, Double> tempTargets) {
    this.enabled = enabled;
    this.ports = ports;
    this.data = data;
    this.tempTargets = tempTargets;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Database database = (Database) o;
    return enabled == database.enabled
        && Objects.equals(ports, database.ports)
        && Objects.equals(data, database.data)
        && Objects.equals(tempTargets, database.tempTargets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enabled, ports, data, tempTargets);
  }

  @Override
  public String toString() {
    return "Database{"
        + "enabled="
        + enabled
        + ", ports="
        + ports
        + ", data="
        + data
        + ", tempTargets="
        + tempTargets
        + '}';
  }
}
