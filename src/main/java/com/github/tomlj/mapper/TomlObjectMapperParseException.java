package com.github.tomlj.mapper;

import java.util.List;
import java.util.Objects;
import org.tomlj.TomlParseError;
import org.tomlj.TomlParseResult;

public final class TomlObjectMapperParseException extends TomlObjectMapperException {
  private final transient TomlParseResult result;

  TomlObjectMapperParseException(TomlParseResult result) {
    this.result = Objects.requireNonNull(result, "TOML parser result must not be null");
  }

  public List<TomlParseError> errors() {
    return result.errors();
  }
}
