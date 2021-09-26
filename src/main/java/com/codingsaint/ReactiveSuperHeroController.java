package com.codingsaint;

import com.codingsaint.domain.Superhero;
import com.codingsaint.service.SuperheroService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Controller("rx/")
public class ReactiveSuperHeroController {
    private static final Logger logger = LoggerFactory.getLogger(SuperHeroController.class);

    private final SuperheroService service;

    public ReactiveSuperHeroController(SuperheroService service) {
        this.service = service;
    }

    @Get("superheroes")
    public Flux<Superhero> superheroes() {
        return service.superheroes();
    }


    @Get("superhero/{id}")
    public Mono<Superhero> superheroesById(Long id) {

        return service.superheroesById(id);

    }

    @Post("/superhero")
    Single<Superhero> create(@Valid Superhero superhero) {
        logger.info("Call to add a new saviour {} ", superhero);
        return Single.fromPublisher(service.create(superhero));
    }

    @Put("superhero")
    Single<Superhero> update(@Valid Superhero superhero) {
        return Single.fromPublisher(service.update(superhero, superhero.id()));
    }

    @Delete("superhero/{id}")
    Single<HttpResponse<?>> delete(@NotNull Long id) {
        return Single
                .fromPublisher(service.delete(id))
                .map(deleted -> deleted > 0 ? HttpResponse.noContent() : HttpResponse.notFound());
    }


}
