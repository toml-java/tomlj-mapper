package com.github.tomlj.mapper.model;

import java.util.Objects;

public class Server {
  private final String ip;
  private final String role;

  public Server(String ip, String role) {
    this.ip = ip;
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Server server = (Server) o;
    return Objects.equals(ip, server.ip) && Objects.equals(role, server.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ip, role);
  }

  @Override
  public String toString() {
    return "Server{" + "ip='" + ip + '\'' + ", role='" + role + '\'' + '}';
  }
}
