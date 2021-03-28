package org.tomlj.mapper;

import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.io.InputStream;

public final class TomlObjectMapper<T> {

  private final org.tomlj.mapper.TomlObjectFactory<T> factory;
  private final org.tomlj.mapper.TomlObjectFactoryRegistry registry;

  private TomlObjectMapper(Class<T> clazz, org.tomlj.mapper.TomlObjectFactoryRegistry registry) {
    this.registry = registry;
    factory = registry.factory(clazz);
  }

  private TomlObjectMapper(org.tomlj.mapper.TomlTypeReference<T> typeReference, org.tomlj.mapper.TomlObjectFactoryRegistry registry) {
    this.registry = registry;
    factory = registry.factory(typeReference);
  }

  private TomlObjectMapper(Class<T> clazz) {
    this(clazz, new org.tomlj.mapper.TomlObjectFactoryRegistry());
  }

  private TomlObjectMapper(org.tomlj.mapper.TomlTypeReference<T> typeReference) {
    this(typeReference, new org.tomlj.mapper.TomlObjectFactoryRegistry());
  }

  public static <T> TomlObjectMapper<T> forType(org.tomlj.mapper.TomlTypeReference<T> typeReference) {
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
