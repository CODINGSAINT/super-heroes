package com.codingsaint;

import com.github.javafaker.Faker;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class SuperHeroController {

    @Get("super-heroes")
    public Collection<String> superheroes(){
        Faker fake = new Faker();
        List<String> superHeros= new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            var superHero=fake.superhero();
            superHeros.add(superHero.name()+" - "+superHero.power());
        }
        return superHeros;
    }
}
