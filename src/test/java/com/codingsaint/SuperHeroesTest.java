package com.codingsaint;

import com.codingsaint.domain.Superhero;
import com.github.javafaker.Faker;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.StreamingHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MicronautTest
class SuperHeroesTest {
    static PostgreSQLContainer postGres = new PostgreSQLContainer("postgres:12");
    @Inject
    EmbeddedApplication<?> application;
    Superhero superhero = null;

    @BeforeAll
    static void start() {
        postGres.start();
    }

    @AfterAll
    static void tearDown() throws InterruptedException {
        if (postGres != null)
            postGres.stop();
    }

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    @Inject
    @Client("/")
    private StreamingHttpClient client;


    @Test
    @Order(1)
    void insert() {
        var faker = new Faker();
        var superHero = faker.superhero();
        String name = superHero.name();
        String prefix = superHero.prefix();
        String suffix = superHero.suffix();
        String power = superHero.power();
        superhero = new Superhero(null, name, prefix, suffix, power);
        var request = HttpRequest.POST("/rx/superhero", superhero);
        var response = client.toBlocking().exchange(request, Superhero.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatus());
        Assert.assertEquals(name, response.body().name());
        Assert.assertEquals(prefix, response.body().prefix());
    }

    @Test
    @Order(2)
    void get() {
        var request = HttpRequest.GET("/rx/superhero/1");
        var response = client.toBlocking().exchange(request, Superhero.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatus());
        Assert.assertEquals(superhero.name(), response.body().name());
    }

    @Test
    @Order(3)
    void update() {
        String name = superhero.name() + "_Modified";
        String prefix = superhero.prefix() + "_Modified";
        Superhero hero = new Superhero(1L, name, prefix, superhero.suffix(), superhero.power());
        var request = HttpRequest.PUT("/rx/superhero", hero);
        var response = client.toBlocking().exchange(request, Superhero.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatus());
        Assert.assertEquals(name, response.body().name());
        Assert.assertEquals(prefix, response.body().prefix());
    }

    @Test
    @Order(4)
    void delete() {
        var request = HttpRequest.DELETE("/rx/superhero/1");
        var response = client.toBlocking().exchange(request);
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }


}
