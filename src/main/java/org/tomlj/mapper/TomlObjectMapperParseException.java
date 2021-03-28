package org.tomlj.mapper;

import org.tomlj.TomlParseError;
import org.tomlj.TomlParseResult;

import java.util.List;
import java.util.Objects;

public final class TomlObjectMapperParseException extends org.tomlj.mapper.TomlObjectMapperException {
  private final transient TomlParseResult result;

  TomlObjectMapperParseException(TomlParseResult result) {
    this.result = Objects.requireNonNull(result, "TOML parser result must not be null");
  }

  public List<TomlParseError> errors() {
    return result.errors();
  }
}
