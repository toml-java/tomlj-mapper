package com.github.tomlj.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.tomlj.mapper.model.Car;
import com.github.tomlj.mapper.model.Color;
import com.github.tomlj.mapper.model.CustomTypes;
import com.github.tomlj.mapper.model.Database;
import com.github.tomlj.mapper.model.EnumCar;
import com.github.tomlj.mapper.model.EnumColor;
import com.github.tomlj.mapper.model.Owner;
import com.github.tomlj.mapper.model.Products;
import com.github.tomlj.mapper.model.RequiredField;
import com.github.tomlj.mapper.model.Server;
import com.github.tomlj.mapper.model.Simple;
import com.github.tomlj.mapper.model.SimpleRename;
import com.github.tomlj.mapper.model.TomlExample;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.Test;

class TomlObjectMapperTest {

  @Test
  void simple() throws IOException {
    // Given
    try (InputStream inputStream = getClass().getResourceAsStream("/simple.toml")) {
      TomlObjectMapper<Simple> mapper = TomlObjectMapper.forClass(Simple.class);

      // When
      Simple simple = mapper.parse(inputStream);

      // Then
      assertEquals(new Simple("value1", Arrays.asList(1L, 2L, 3L, 4L)), simple);
    }
  }

  @Test
  void required() throws IOException {
    // Given
    try (InputStream inputStream = getClass().getResourceAsStream("/simple.toml")) {
      TomlObjectMapper<RequiredField> mapper = TomlObjectMapper.forClass(RequiredField.class);

      // When
      assertThrows(
          TomlObjectMapperRequiredPropertyException.class,
          () -> {
            RequiredField simple = mapper.parse(inputStream);
          });
    }
  }

  @Test
  void simpleRename() throws IOException {
    // Given
    try (InputStream inputStream = getClass().getResourceAsStream("/simple_rename.toml")) {
      TomlObjectMapper<SimpleRename> mapper = TomlObjectMapper.forClass(SimpleRename.class);

      // When
      SimpleRename simpleRename = mapper.parse(inputStream);

      // Then
      assertEquals(new SimpleRename("value1", Arrays.asList(1L, 2L, 3L, 4L)), simpleRename);
    }
  }

  @Test
  void canonical() throws IOException {
    // Given
    try (InputStream inputStream = getClass().getResourceAsStream("/canonical.toml")) {
      TomlObjectMapper<TomlExample> mapper = TomlObjectMapper.forClass(TomlExample.class);
      Server alphaServer = new Server("10.0.0.1", "frontend");
      Server betaServer = new Server("10.0.0.2", "backend");
      Map<String, Server> servers = new LinkedHashMap<>();
      servers.put("alpha", alphaServer);
      servers.put("beta", betaServer);
      Map<String, Double> tempTargets = new LinkedHashMap<>();
      tempTargets.put("cpu", 79.5);
      tempTargets.put("case", 72.0);
      Database database =
          new Database(
              true,
              Arrays.asList(8000L, 8001L, 8002L),
              Arrays.asList(Arrays.asList("delta", "phi"), Collections.singletonList(3.14)),
              tempTargets);
      Owner owner =
          new Owner("Tom Preston-Werner", OffsetDateTime.parse("1979-05-27T07:32:00-08:00"));
      TomlExample expected = new TomlExample("TOML Example", owner, database, servers);

      // When
      TomlExample tomlExample = mapper.parse(inputStream);

      // Then
      assertEquals(expected, tomlExample);
    }
  }

  @Test
  void map() throws IOException {
    // Given
    try (InputStream inputStream = getClass().getResourceAsStream("/map.toml")) {
      TomlObjectMapper<Map<String, String>> mapper =
          TomlObjectMapper.forType(new TomlTypeReference<Map<String, String>>() {});
      Map<String, String> expected = new HashMap<>();
      expected.put("key1", "value 1");
      expected.put("key2", "value 2");
      expected.put("key3", "value 3");

      // When
      Map<String, String> map = mapper.parse(inputStream);

      // Then
      assertEquals(expected, map);
    }
  }

  @Test
  void treeMap() throws IOException {
    // Given
    try (InputStream inputStream = getClass().getResourceAsStream("/map.toml")) {
      TomlObjectMapper<TreeMap<String, String>> mapper =
          TomlObjectMapper.forType(new TomlTypeReference<TreeMap<String, String>>() {});
      Map<String, String> expected = new TreeMap<>();
      expected.put("key1", "value 1");
      expected.put("key2", "value 2");
      expected.put("key3", "value 3");

      // When
      TreeMap<String, String> map = mapper.parse(inputStream);

      // Then
      assertEquals(expected, map);
    }
  }

  @Test
  void arrayOfTables() throws IOException {
    // Given
    try (InputStream simpleTomlInputStream =
        getClass().getResourceAsStream("/array_of_tables.toml")) {
      TomlObjectMapper<Map<String, List<Products>>> mapper =
          TomlObjectMapper.forType(new TomlTypeReference<Map<String, List<Products>>>() {});

      // When
      Map<String, List<Products>> productsMap = mapper.parse(simpleTomlInputStream);

      // Then
      assertEquals(
          Collections.singletonMap(
              "products",
              Arrays.asList(
                  new Products("Hammer", 738594937L, null),
                  new Products(null, null, null),
                  new Products("Nail", 284758393L, "gray"))),
          productsMap);
    }
  }

  @Test
  void customTypes() throws IOException {
    // Given
    try (InputStream inputStream = getClass().getResourceAsStream("/custom_types.toml")) {
      TomlObjectMapper<CustomTypes> mapper = TomlObjectMapper.forClass(CustomTypes.class);

      // When
      CustomTypes simple = mapper.parse(inputStream);

      // Then
      TreeMap<String, String> treeMap = new TreeMap<>();
      treeMap.put("a", "b");
      treeMap.put("b", "c");
      assertEquals(
          new CustomTypes(new LinkedList<>(Arrays.asList("a", "b", "c")), treeMap), simple);
    }
  }

  @Test
  void enumColor() throws IOException {
    // Given
    try (InputStream inputStream = getClass().getResourceAsStream("/enum-color.toml")) {
      TomlObjectMapper<EnumColor> mapper = TomlObjectMapper.forClass(EnumColor.class);

      // When
      EnumColor enumColor = mapper.parse(inputStream);

      // Then
      assertEquals(new EnumColor(Color.RED), enumColor);
    }
  }

  @Test
  void enumCar() throws IOException {
    // Given
    try (InputStream inputStream = getClass().getResourceAsStream("/enum-car.toml")) {
      TomlObjectMapper<EnumCar> mapper = TomlObjectMapper.forClass(EnumCar.class);

      // When
      EnumCar enumCar = mapper.parse(inputStream);

      // Then
      assertEquals(new EnumCar(Car.BMW, Car.AUDI, Car.TESLA), enumCar);
    }
  }
}
