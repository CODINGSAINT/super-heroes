package com.codingsaint;

import com.codingsaint.domain.Superhero;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.StreamingHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.*;

import jakarta.inject.Inject;
import org.testcontainers.junit.jupiter.Testcontainers;

@MicronautTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SuperHeroesTest {

    @Inject
    EmbeddedApplication<?> application;

    static DBTestContainer postGres = null;

    @BeforeAll
    static void init() {
        postGres = new DBTestContainer();
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
    StreamingHttpClient client;


    @Test
    @Order(1)
    void insert() {
        Superhero hero = new Superhero(null, "Razer Blade", "XSPEED", "Ligthenin", "Speed Run");
        var request = HttpRequest.POST("/rx/superhero", hero);
        var response = client.toBlocking().exchange(request, Superhero.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatus());
        System.out.println(response.body());
        Assert.assertEquals("Razer Blade", response.body().name());
        Assert.assertEquals("XSPEED", response.body().prefix());
    }

    @Test
    @Order(2)
    void get() {
        var request = HttpRequest.GET("/rx/superhero/1");
        var response = client.toBlocking().exchange(request, Superhero.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatus());
        System.out.println(response.body());
        Assert.assertEquals("Razer Blade", response.body().name());
        //  Assert.assertEquals("XSPEED", response.body().prefix());
    }

    @Test
    @Order(3)
    void update() {
        Superhero hero = new Superhero(1L, "Razer Blade", "XSPEED2", "Ligthenin", "Speed Run");
        var request = HttpRequest.PUT("/rx/superhero", hero);
        var response = client.toBlocking().exchange(request, Superhero.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatus());
        System.out.println(response.body());
        Assert.assertEquals("Razer Blade", response.body().name());
        Assert.assertEquals("XSPEED2", response.body().prefix());
    }

    @Test
    @Order(4)
    void delete() {
        var request = HttpRequest.DELETE("/rx/superhero/1");
        var response = client.toBlocking().exchange(request);
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
    }


}
