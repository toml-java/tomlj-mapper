package org.tomlj.mapper;

import org.tomlj.mapper.accessor.AccessorType;
import org.tomlj.mapper.accessor.DefaultTomlObjectAccessor;
import org.tomlj.mapper.accessor.DelegateTomlObjectAccessor;
import org.tomlj.mapper.accessor.FactoryTomlObjectAccessor;
import org.tomlj.mapper.accessor.GenericTomlObjectAccessor;
import org.tomlj.mapper.accessor.ListTomlObjectAccessor;
import org.tomlj.mapper.accessor.MapTomlObjectAccessor;
import org.tomlj.mapper.factory.AccessorBasedTomlFactory;
import org.tomlj.mapper.factory.ConstructorBasedTomlFactory;
import org.tomlj.mapper.factory.DelegateTomlFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class TomlObjectFactoryRegistry {
  private static final List<Class<?>> DEFAULT_ACCESSORS =
      Arrays.asList(
          String.class,
          Long.class,
          long.class,
          Double.class,
          double.class,
          Boolean.class,
          boolean.class,
          OffsetDateTime.class,
          LocalDateTime.class,
          LocalDate.class,
          LocalTime.class);

  private final Map<Type, org.tomlj.mapper.TomlObjectFactory<?>> factories;
  private final Map<AccessorType, TomlObjectAccessor<?>> accessors;

  public TomlObjectFactoryRegistry(
      Map<Type, org.tomlj.mapper.TomlObjectFactory<?>> factories,
      Map<AccessorType, TomlObjectAccessor<?>> accessors) {
    this.factories = factories;
    this.accessors = accessors;
  }

  public TomlObjectFactoryRegistry() {
    this(new HashMap<>(), new HashMap<>());
  }

  public <T> org.tomlj.mapper.TomlObjectFactory<T> factory(Class<T> aClass) {
    return lookupFactoryOrCompute(aClass, this::createFactoryForClass);
  }

  public <T> org.tomlj.mapper.TomlObjectFactory<T> factory(TomlTypeReference<T> typeReference) {
    return lookupFactoryOrCompute(typeReference.getType(), this::createFactoryForParameterizedType);
  }

  @SuppressWarnings("unchecked")
  public <T> TomlObjectAccessor<T> accessor(Class<T> aClass, Type type) {
    AccessorType accessorType = new AccessorType(aClass, type);
    TomlObjectAccessor<T> result = (TomlObjectAccessor<T>) accessors.get(accessorType);

    if (result == null) {
      DelegateTomlObjectAccessor<T> delegateTomlObjectAccessor = new DelegateTomlObjectAccessor<>();
      accessors.put(accessorType, delegateTomlObjectAccessor);
      result = (TomlObjectAccessor<T>) createAccessorForClass(aClass, type);
      delegateTomlObjectAccessor.setDelegate(result);
      accessors.put(accessorType, result);
    }

    return result;
  }

  private org.tomlj.mapper.TomlObjectFactory<?> createFactoryForClass(Type type) {
    Constructor<?>[] constructors = ((Class<?>) type).getConstructors();
    if (constructors.length == 0) {
      throw new TomlObjectMapperException("No constructors found!");
    }

    return new ConstructorBasedTomlFactory<>(this, constructors[0]);
  }

  private <T> TomlObjectAccessor<?> createAccessorForClass(Class<T> aClass, Type type) {
    if (DEFAULT_ACCESSORS.contains(aClass)) {
      return new DefaultTomlObjectAccessor<>();
    } else if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
      if (aClass.isAssignableFrom(List.class)) {
        TomlObjectAccessor<?> itemsAccessor = tomlObjectAccessorFrom(actualTypeArguments[0]);
        return new ListTomlObjectAccessor<>(itemsAccessor);
      } else if (aClass.isAssignableFrom(Map.class)) {
        return mapTomlObjectAccessor(actualTypeArguments);
      } else {
        throw new TomlObjectMapperException("Unsupported parameterized type: " + aClass.getName());
      }
    } else if (!aClass.equals(Object.class)) {
      org.tomlj.mapper.TomlObjectFactory<T> factory = factory(aClass);
      return new FactoryTomlObjectAccessor<>(factory);
    }

    return new GenericTomlObjectAccessor();
  }

  private MapTomlObjectAccessor<?, ?> mapTomlObjectAccessor(Type[] actualTypeArguments) {
    TomlObjectAccessor<?> keyAccessor = tomlObjectAccessorFrom(actualTypeArguments[0]);
    TomlObjectAccessor<?> valueAccessor = tomlObjectAccessorFrom(actualTypeArguments[1]);
    return new MapTomlObjectAccessor<>(keyAccessor, valueAccessor);
  }

  private TomlObjectAccessor<?> tomlObjectAccessorFrom(Type type) {
    if (type instanceof Class<?>) {
      return createAccessorForClass((Class<?>) type, null);
    } else if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      return accessor((Class<?>) parameterizedType.getRawType(), type);
    }
    throw new TomlObjectMapperException("Could not create accessor for type: " + type);
  }

  @SuppressWarnings("unchecked")
  private <T, K extends Type> org.tomlj.mapper.TomlObjectFactory<T> lookupFactoryOrCompute(
      K aClass, Function<K, org.tomlj.mapper.TomlObjectFactory<?>> computeFunction) {
    org.tomlj.mapper.TomlObjectFactory<T> result = (org.tomlj.mapper.TomlObjectFactory<T>) factories.get(aClass);

    if (result == null) {
      DelegateTomlFactory<T> delegateTomlFactory = new DelegateTomlFactory<>();
      factories.put(aClass, delegateTomlFactory);
      result = (org.tomlj.mapper.TomlObjectFactory<T>) computeFunction.apply(aClass);
      delegateTomlFactory.setDelegate(result);
      factories.put(aClass, result);
    }

    return result;
  }

  private org.tomlj.mapper.TomlObjectFactory<?> createFactoryForParameterizedType(Type type) {
    ParameterizedType parameterizedType = (ParameterizedType) type;
    Type rawType = parameterizedType.getRawType();
    if (Map.class.equals(rawType)) {
      return new AccessorBasedTomlFactory<>(
          mapTomlObjectAccessor(parameterizedType.getActualTypeArguments()));
    }
    throw new TomlObjectMapperException(
        "Parameterized type " + type.getTypeName() + " is not supported.");
  }
}
