package com.codingsaint;

import com.codingsaint.domain.Superhero;
import com.codingsaint.repository.ReactiveSuperheroRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.reactivex.Single;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Controller("rx/")
public class ReactiveSuperHeroController {
    private ReactiveSuperheroRepository reactiveSuperheroRepository;

    public ReactiveSuperHeroController(ReactiveSuperheroRepository reactiveSuperheroRepository) {
        this.reactiveSuperheroRepository = reactiveSuperheroRepository;
    }

    @Get("superheroes")
    public Flux<Superhero> superheroes() {
        return reactiveSuperheroRepository.findAll();
    }

    @Get("superhero/{id}")
    public Mono<Superhero> superheroesById(Long id) {
        return reactiveSuperheroRepository.findById(id);

    }

    @Post("/superhero")
    Single<Superhero> create(@Valid Superhero superhero) {
        return Single.fromPublisher(reactiveSuperheroRepository.save(superhero));
    }

    @Put("superhero")
    Single<Superhero> update( @Valid Superhero superhero) {
        return Single.fromPublisher(reactiveSuperheroRepository.update(superhero));
    }
    @Delete("superhero/{id}")
    Single<HttpResponse<?>> update(@NotNull Long id) {
        return Single
                .fromPublisher(reactiveSuperheroRepository.deleteById(id))
                .map(deleted->deleted>0?HttpResponse.noContent():HttpResponse.notFound());
    }


}
