package com.codingsaint.service;

import com.codingsaint.domain.Superhero;
import com.codingsaint.repository.ReactiveSuperheroRepository;
import io.micronaut.cache.CacheManager;
import io.micronaut.cache.annotation.CacheConfig;
import io.micronaut.cache.annotation.CacheInvalidate;
import io.micronaut.cache.annotation.CachePut;
import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Singleton
@CacheConfig("superheroes")
public class SuperheroService {
    private static final Logger logger = LoggerFactory.getLogger(SuperheroService.class);
    protected ReactiveSuperheroRepository reactiveSuperheroRepository;
    @Inject
    private CacheManager cacheManager;
    SuperheroService(ReactiveSuperheroRepository superheroRepository) {
        this.reactiveSuperheroRepository = superheroRepository;

    }


    public Flux<Superhero> superheroes() {
        logger.info("Fetching all of the superheroes in universe ");
        return reactiveSuperheroRepository.findAll();
    }


    @Cacheable( cacheNames = {"superheroes"}, parameters ={"id"}  )
    public Mono<Superhero> superheroesById(Long id) {
        logger.info("Finding the saviour  id {} ", id);
        return reactiveSuperheroRepository.findById(id);

    }

    public Publisher<Superhero> create(Superhero superhero) {
        logger.info("Adding a new saviour {} ", superhero);
        var created = reactiveSuperheroRepository.save(superhero);
        return created;
    }

    @CachePut( cacheNames = "superheroes", parameters = "id")
    public Publisher<Superhero> update(Superhero superhero, Long id) {
        logger.info("Updating the old saviour {} ", superhero);
        var updated=reactiveSuperheroRepository.update(superhero);

        return updated;
    }

    @CacheInvalidate(cacheNames = {"superheroes"},parameters = "id" ,async = true)
    public Publisher<Long> delete(Long id) {
        var deleted= reactiveSuperheroRepository.deleteById(id);

        return deleted;
    }


}
