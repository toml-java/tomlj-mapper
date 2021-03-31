# An object mapper for TOML format

![Maven Central](https://img.shields.io/maven-central/v/com.github.toml-java/tomlj-mapper)

## Legal

Dual-licensed under `MIT` or the [UNLICENSE](http://unlicense.org/).

## Usage

pom.xml:

```xml
<dependency>
    <groupId>com.github.toml-java</groupId>
    <artifactId>tomlj-mapper</artifactId>
    <version>0.2.0</version>
</dependency>
```

simple.toml:

```toml
key1 = 'value1'
array1 = [1, 2, 3, 4]
```

Test.java:

```java
import com.github.tomlj.mapper.TomlObjectMapper;

class Test {
    @Test
    public parseSimple() {
        try (InputStream inputStream = getClass().getResourceAsStream("/simple.toml")) {
            TomlObjectMapper<Simple> mapper = TomlObjectMapper.forClass(Simple.class);

            // When
            Simple simple = mapper.parse(inputStream);

            // Then
            assertEquals(new Simple("value1", Arrays.asList(1L, 2L, 3L, 4L)), simple);
        }
    }
}
```

Simple.java:

```java
public class Simple {
    private final String key1;
    private final List<Long> array1;

    public Simple(String key1, List<Long> array1) {
        this.key1 = key1;
        this.array1 = array1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Simple simple = (Simple) o;
        return Objects.equals(key1, simple.key1) && Objects.equals(array1, simple.array1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key1, array1);
    }

    @Override
    public String toString() {
        return "Simple{" + "key1='" + key1 + '\'' + ", array1=" + array1 + '}';
    }
}
```

## Links

- <https://toml.io/>
- <https://github.com/tomlj/tomlj>
