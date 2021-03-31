package com.github.tomlj.mapper;

import com.github.tomlj.mapper.accessor.AccessorType;
import com.github.tomlj.mapper.accessor.DefaultTomlObjectAccessor;
import com.github.tomlj.mapper.accessor.DelegateTomlObjectAccessor;
import com.github.tomlj.mapper.accessor.FactoryTomlObjectAccessor;
import com.github.tomlj.mapper.accessor.GenericTomlObjectAccessor;
import com.github.tomlj.mapper.accessor.ListTomlObjectAccessor;
import com.github.tomlj.mapper.accessor.MapTomlObjectAccessor;
import com.github.tomlj.mapper.factory.AccessorBasedTomlFactory;
import com.github.tomlj.mapper.factory.ConstructorBasedTomlFactory;
import com.github.tomlj.mapper.factory.DefaultInstanceCreatorFactory;
import com.github.tomlj.mapper.factory.DelegateTomlFactory;
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

  private final Map<Type, TomlObjectFactory<?>> factories;
  private final Map<AccessorType, TomlObjectAccessor<?>> accessors;
  private final TomlObjectInstanceCreatorFactory instanceCreatorFactory;

  public TomlObjectFactoryRegistry(
      Map<Type, TomlObjectFactory<?>> factories,
      Map<AccessorType, TomlObjectAccessor<?>> accessors,
      TomlObjectInstanceCreatorFactory instanceCreatorFactory) {
    this.factories = factories;
    this.accessors = accessors;
    this.instanceCreatorFactory = instanceCreatorFactory;
  }

  public TomlObjectFactoryRegistry() {
    this(new HashMap<>(), new HashMap<>(), new DefaultInstanceCreatorFactory());
  }

  public <T> TomlObjectFactory<T> factory(Class<T> aClass) {
    return lookupFactoryOrCompute(aClass, this::createFactoryForClass);
  }

  public <T> TomlObjectFactory<T> factory(TomlTypeReference<T> typeReference) {
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

  private TomlObjectFactory<?> createFactoryForClass(Type type) {
    Constructor<?>[] constructors = ((Class<?>) type).getConstructors();
    if (constructors.length == 0) {
      throw new TomlObjectMapperException("No constructors found!");
    }

    return new ConstructorBasedTomlFactory<>(this, constructors[0]);
  }

  @SuppressWarnings("unckecked")
  private <T> TomlObjectAccessor<?> createAccessorForClass(Class<T> aClass, Type type) {
    if (DEFAULT_ACCESSORS.contains(aClass)) {
      return new DefaultTomlObjectAccessor<>();
    } else if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
      if (List.class.isAssignableFrom(aClass)) {
        TomlObjectAccessor<?> itemsAccessor = tomlObjectAccessorFrom(actualTypeArguments[0]);
        return new ListTomlObjectAccessor(instanceCreatorFactory.forClass(aClass), itemsAccessor);
      } else if (Map.class.isAssignableFrom(aClass)) {
        return mapTomlObjectAccessor(aClass, actualTypeArguments);
      } else {
        throw new TomlObjectMapperException("Unsupported parameterized type: " + aClass.getName());
      }
    } else if (Enum.class.isAssignableFrom(aClass)) {
      throw new UnsupportedOperationException("Enums are not supported");
    } else if (!aClass.equals(Object.class)) {
      TomlObjectFactory<T> factory = factory(aClass);
      return new FactoryTomlObjectAccessor<>(factory);
    }

    return new GenericTomlObjectAccessor();
  }

  @SuppressWarnings("unckecked")
  private MapTomlObjectAccessor<?, ?> mapTomlObjectAccessor(
      Class<?> aClass, Type[] actualTypeArguments) {
    TomlObjectAccessor<?> keyAccessor = tomlObjectAccessorFrom(actualTypeArguments[0]);
    TomlObjectAccessor<?> valueAccessor = tomlObjectAccessorFrom(actualTypeArguments[1]);
    return new MapTomlObjectAccessor(
        instanceCreatorFactory.forClass(aClass), keyAccessor, valueAccessor);
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
  private <T, K extends Type> TomlObjectFactory<T> lookupFactoryOrCompute(
      K aClass, Function<K, TomlObjectFactory<?>> computeFunction) {
    TomlObjectFactory<T> result = (TomlObjectFactory<T>) factories.get(aClass);

    if (result == null) {
      DelegateTomlFactory<T> delegateTomlFactory = new DelegateTomlFactory<>();
      factories.put(aClass, delegateTomlFactory);
      result = (TomlObjectFactory<T>) computeFunction.apply(aClass);
      delegateTomlFactory.setDelegate(result);
      factories.put(aClass, result);
    }

    return result;
  }

  private TomlObjectFactory<?> createFactoryForParameterizedType(Type type) {
    ParameterizedType parameterizedType = (ParameterizedType) type;
    Type rawType = parameterizedType.getRawType();
    Class<?> instanceClass = null;
    if (Map.class.equals(rawType)) {
      instanceClass = Map.class;
    } else if (rawType instanceof Class<?> && Map.class.isAssignableFrom((Class<?>) rawType)) {
      instanceClass = (Class<?>) rawType;
    }
    if (instanceClass == null) {
      throw new TomlObjectMapperException(
          "Parameterized type " + type.getTypeName() + " is not supported.");
    }
    return new AccessorBasedTomlFactory<>(
        mapTomlObjectAccessor(instanceClass, parameterizedType.getActualTypeArguments()));
  }
}
