package com.codingsaint;

import com.codingsaint.domain.Superhero;
import com.github.javafaker.Faker;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpHeaders;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.StreamingHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.simple.SimpleHttpHeaders;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MicronautTest
class SuperHeroesTest {

    @Inject
    EmbeddedApplication<?> application;
    Superhero superhero = null;

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
        addHeaders(request);
        var response = client.toBlocking().exchange(request, Superhero.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatus());
        Assert.assertEquals(name, response.body().name());
        Assert.assertEquals(prefix, response.body().prefix());
    }

    @Test
    @Order(2)
    void get() {
        var request = HttpRequest.GET("/rx/superhero/1");
        addHeaders(request);
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
        addHeaders(request);
        var response = client.toBlocking().exchange(request, Superhero.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatus());
        Assert.assertEquals(name, response.body().name());
        Assert.assertEquals(prefix, response.body().prefix());
    }

    @Test
    @Order(4)
    void delete() {
        var request = HttpRequest.DELETE("/rx/superhero/1");
        addHeaders(request);
        var response = client.toBlocking().exchange(request);
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }

    private void addHeaders(MutableHttpRequest request){
        MutableHttpHeaders headers = request.getHeaders();
        headers.add("TRACKING_ID", UUID.randomUUID().toString());
        headers.add("USER_AGENT", "TEST Client Agent 0.1 ");

    }

}
