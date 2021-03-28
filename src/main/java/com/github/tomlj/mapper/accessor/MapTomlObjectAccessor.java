package com.github.tomlj.mapper.accessor;

import com.github.tomlj.mapper.TomlObjectAccessor;
import com.github.tomlj.mapper.TomlObjectFactoryRegistry;
import java.util.Map;
import java.util.stream.Collectors;
import org.tomlj.TomlTable;

public final class MapTomlObjectAccessor<K, V> implements TomlObjectAccessor<Map<K, V>> {
  private final TomlObjectAccessor<K> keyAccessor;
  private final TomlObjectAccessor<V> valueAccessor;

  public MapTomlObjectAccessor(
      TomlObjectAccessor<K> keyAccessor, TomlObjectAccessor<V> valueAccessor) {
    this.keyAccessor = keyAccessor;
    this.valueAccessor = valueAccessor;
  }

  @Override
  public Map<K, V> apply(TomlObjectFactoryRegistry registry, Object value) {
    TomlTable tomlTable = (TomlTable) value;
    return tomlTable.keySet().stream()
        .collect(
            Collectors.toMap(
                key -> keyAccessor.apply(registry, key),
                key -> valueAccessor.apply(registry, tomlTable.get(key))));
  }
}
