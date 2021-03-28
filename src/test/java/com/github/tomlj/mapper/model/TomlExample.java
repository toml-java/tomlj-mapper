package com.github.tomlj.mapper.model;

import java.util.Map;
import java.util.Objects;

public class TomlExample {
  private final String title;
  private final Owner owner;
  private final Database database;
  private final Map<String, Server> servers;

  public TomlExample(String title, Owner owner, Database database, Map<String, Server> servers) {
    this.title = title;
    this.owner = owner;
    this.database = database;
    this.servers = servers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TomlExample that = (TomlExample) o;
    return Objects.equals(title, that.title)
        && Objects.equals(owner, that.owner)
        && Objects.equals(database, that.database)
        && Objects.equals(servers, that.servers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, owner, database, servers);
  }

  @Override
  public String toString() {
    return "TomlExample{"
        + "title='"
        + title
        + '\''
        + ", owner="
        + owner
        + ", database="
        + database
        + ", servers="
        + servers
        + '}';
  }
}
