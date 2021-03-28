package org.tomlj.mapper;

public class TomlObjectMapperException extends RuntimeException {
    public TomlObjectMapperException(Exception exception) {
        super(exception);
    }

    public TomlObjectMapperException(String message) {
        super(message);
    }

    public TomlObjectMapperException() {
        super();
    }
}
