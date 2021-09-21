package com.codingsaint;

import com.codingsaint.domain.Superhero;
import com.codingsaint.repository.SuperheroRepository;
import com.github.javafaker.Faker;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Controller
@ExecuteOn(TaskExecutors.IO)
public class SuperHeroController {
    private static final Logger logger = LoggerFactory.getLogger(SuperHeroController.class);
    protected SuperheroRepository superheroRepository;

    SuperHeroController(SuperheroRepository superheroRepository) {
        this.superheroRepository = superheroRepository;
    }

    /**
     * This just generates Fake Superheroes
     *
     * @return
     */
    @Get("super-heroes")
    public Collection<String> superheroes() {
        Faker fake = new Faker();
        var superHeros = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            var superHero = fake.superhero();
            superHeros.add(superHero.name() + " - " + superHero.power());
        }
        return superHeros;
    }

    @Post("superhero")
    public HttpResponse<Superhero> save(@Valid Superhero superhero) {
        logger.info("Saving new Superhero {}", superhero);
        var superHero = superheroRepository.save(superhero);
        return HttpResponse.created(superHero)
                .headers(headers -> headers.location(URI("/superhero/" + "" + superHero.id())));

    }

    @Get("superhero/{id}")
    public Optional<Superhero> get(Long id) {
        return superheroRepository.findById(id);
    }

    @Put("superhero")
    public HttpResponse update(@Body @Valid Superhero superhero) {
        superheroRepository.update(superhero);
        return HttpResponse
                .noContent()
                .headers(headers ->
                        headers.location(URI("/superhero/" + "" + superhero.id())));
    }

    @Delete("superhero/{id}")
    @Status(HttpStatus.NO_CONTENT)
    public void delete(Long id) {
        superheroRepository.deleteById(id);
    }

    private URI URI(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
            return uri;
        } catch (URISyntaxException e) {
            logger.error("Error while creating URL {}", e);
        }
        return uri;
    }

}
