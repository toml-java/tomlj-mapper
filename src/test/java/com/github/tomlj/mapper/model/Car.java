package com.github.tomlj.mapper.model;

import com.github.tomlj.mapper.TomlRename;

public enum Car {
  AUDI,
  BMW,
  @TomlRename("Tesla")
  TESLA
}
