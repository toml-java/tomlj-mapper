package com.github.tomlj.mapper.factory;

import com.github.tomlj.mapper.TomlObjectInstanceCreator;
import com.github.tomlj.mapper.TomlObjectInstanceCreatorFactory;
import com.github.tomlj.mapper.TomlObjectMapperException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class DefaultInstanceCreatorFactory implements TomlObjectInstanceCreatorFactory {

  @SuppressWarnings("unchecked")
  @Override
  public <T> TomlObjectInstanceCreator<T> forClass(Class<T> aClass) {
    if (List.class.equals(aClass)) {
      return () -> (T) new ArrayList<>();
    }
    if (Map.class.equals(aClass)) {
      return () -> (T) new HashMap<>();
    }
    if (Set.class.equals(aClass)) {
      return () -> (T) new HashSet<>();
    }
    if (SortedMap.class.equals(aClass) || NavigableMap.class.equals(aClass)) {
      return () -> (T) new TreeMap<>();
    }
    if (SortedSet.class.equals(aClass) || NavigableSet.class.equals(aClass)) {
      return () -> (T) new TreeSet<>();
    }
    if (aClass.isInterface()) {
      throw new TomlObjectMapperException("Unsupported interface type: " + aClass.getName());
    }

    try {
      Constructor<T> declaredConstructor = aClass.getDeclaredConstructor();
      return () -> {
        try {
          return declaredConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
          throw new RuntimeException(e);
        }
      };
    } catch (NoSuchMethodException | SecurityException e) {
      throw new RuntimeException(e);
    }
  }
}
