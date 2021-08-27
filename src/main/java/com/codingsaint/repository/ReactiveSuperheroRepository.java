package com.codingsaint.repository;

import com.codingsaint.domain.Superhero;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface ReactiveSuperheroRepository extends ReactiveStreamsCrudRepository<Superhero, Long> {

    @NonNull
    @Override
    Mono<Superhero> findById(@NonNull Long aLong);

    @NonNull
    @Override
    Flux<Superhero> findAll();
}
