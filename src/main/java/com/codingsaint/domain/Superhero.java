package com.codingsaint.domain;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

@MappedEntity
public record Superhero(@Id @GeneratedValue @Nullable Long id,
                        String name,
                        String prefix,
                        String suffix,
                        String power) {
}
