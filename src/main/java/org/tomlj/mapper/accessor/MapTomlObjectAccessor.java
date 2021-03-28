package org.tomlj.mapper.accessor;

import org.tomlj.mapper.TomlObjectAccessor;
import org.tomlj.mapper.TomlObjectFactoryRegistry;
import org.tomlj.TomlTable;

import java.util.Map;
import java.util.stream.Collectors;

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
