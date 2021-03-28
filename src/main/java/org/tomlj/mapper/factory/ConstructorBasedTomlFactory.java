package org.tomlj.mapper.factory;

import org.tomlj.mapper.TomlObjectAccessor;
import org.tomlj.mapper.TomlObjectFactory;
import org.tomlj.mapper.TomlObjectFactoryRegistry;
import org.tomlj.mapper.TomlObjectMapperException;
import org.tomlj.mapper.TomlObjectProperty;
import org.tomlj.mapper.TomlProperty;
import org.tomlj.TomlTable;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class ConstructorBasedTomlFactory<T> implements TomlObjectFactory<T> {
  private final Constructor<T> constructor;
  private final Map<String, TomlObjectAccessor<?>> parameterAccessors;
  private final Map<String, Integer> parameterIndexes;
  private final List<TomlObjectProperty> parameterProperties;

  public ConstructorBasedTomlFactory(
      TomlObjectFactoryRegistry registry, Constructor<T> constructor) {
    this.constructor = constructor;
    this.parameterIndexes = new HashMap<>();
    Parameter[] parameters = constructor.getParameters();
    parameterProperties = new ArrayList<>(parameters.length);
    parameterAccessors = new HashMap<>();
    for (int i = 0; i < parameters.length; i++) {
      Parameter parameter = parameters[i];
      TomlProperty tomlProperty = parameter.getAnnotation(TomlProperty.class);
      boolean required;
      String name = null;
      if (tomlProperty != null) {
        required = tomlProperty.required();
        String propertyValue = tomlProperty.value();
        if (!propertyValue.equals("")) {
          name = propertyValue;
        }
      } else {
        required = false;
      }
      if (name == null) {
        if (parameter.isNamePresent()) {
          name = parameter.getName();
        } else {
          throw new TomlObjectMapperException(
              "Name for constructor property not supplied, use either @TomlProperty or -parameters java compiler flag.");
        }
      }
      parameterProperties.add(new TomlObjectProperty(name, required));
      TomlObjectAccessor<?> accessor =
          registry.accessor(
              parameter.getType(),
              Optional.ofNullable(parameter.getAnnotatedType())
                  .map(AnnotatedType::getType)
                  .orElse(null));
      parameterAccessors.put(name, accessor);
      parameterIndexes.put(name, i);
    }
  }

  @Override
  public T createFromTable(TomlObjectFactoryRegistry registry, TomlTable tomlTable) {
    Object[] initargs = new Object[constructor.getParameterCount()];

    for (String key : tomlTable.keySet()) {
      Object value =
          Optional.ofNullable(parameterAccessors.get(key))
              .orElseGet(
                  () -> {
                    throw new TomlObjectMapperException("Cannot find key: " + key);
                  })
              .apply(registry, tomlTable.get(Collections.singletonList(key)));

      initargs[parameterIndexes.get(key)] = value;
    }

    parameterProperties.stream()
        .filter(
            property ->
                property.isRequired() && initargs[parameterIndexes.get(property.getName())] == null)
        .findFirst()
        .ifPresent(
            property -> {
              throw new TomlObjectMapperException(
                  "Required property is missing: " + property.getName());
            });

    try {
      return constructor.newInstance(initargs);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new TomlObjectMapperException(e);
    }
  }
}
