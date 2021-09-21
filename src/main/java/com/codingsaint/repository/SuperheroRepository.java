package com.codingsaint.repository;

import com.codingsaint.domain.Superhero;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface SuperheroRepository extends PageableRepository<Superhero, Long> {
}
