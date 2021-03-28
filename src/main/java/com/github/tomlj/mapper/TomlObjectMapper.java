package com.github.tomlj.mapper;

import java.io.IOException;
import java.io.InputStream;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

public final class TomlObjectMapper<T> {

  private final TomlObjectFactory<T> factory;
  private final TomlObjectFactoryRegistry registry;

  private TomlObjectMapper(Class<T> clazz, TomlObjectFactoryRegistry registry) {
    this.registry = registry;
    factory = registry.factory(clazz);
  }

  private TomlObjectMapper(TomlTypeReference<T> typeReference, TomlObjectFactoryRegistry registry) {
    this.registry = registry;
    factory = registry.factory(typeReference);
  }

  private TomlObjectMapper(Class<T> clazz) {
    this(clazz, new TomlObjectFactoryRegistry());
  }

  private TomlObjectMapper(TomlTypeReference<T> typeReference) {
    this(typeReference, new TomlObjectFactoryRegistry());
  }

  public static <T> TomlObjectMapper<T> forType(TomlTypeReference<T> typeReference) {
    return new TomlObjectMapper<>(typeReference);
  }

  public static <T> TomlObjectMapper<T> forClass(Class<T> clazz) {
    return new TomlObjectMapper<>(clazz);
  }

  public T parse(InputStream inputStream) throws IOException {
    TomlParseResult result = Toml.parse(inputStream);

    if (result.hasErrors()) {
      throw new TomlObjectMapperParseException(result);
    }

    return factory.createFromTable(registry, result);
  }
}
