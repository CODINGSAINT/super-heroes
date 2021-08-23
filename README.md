Micronaut is one of the java framework which can be used to create a Java microservices which are fast, reliable , modular and testable. You can also create serverless application with micronaut. If you have ever worked with Spring or any other java based web framework, you will find very less learning curve with it.

#### Source code :  [https://github.com/CODINGSAINT/super-heroes](https://github.com/CODINGSAINT/super-heroes)
Let's explore micronaut in this series.
## Table of Content
- [Goal](#goal)
- [Prerequisite](#prerequisite)
* [Approach](#approach)
    -  [Creating sample project of micronaut](#approach0)
    - [Running the project](#approach1)
    - [Adding dependency for java faker (Optional )](#approach2)
    - [Creating a sample endpoint for heros](#approach3)

- [Conclusion](#conclusion)

<div id='id-goal'/>
## Goal
The goal of this article is simple. Create a Hello world kind of application with micronaut. The application should build and start . We will also create a custom (GET) endpoint to see it working . Our endpoint will use Faker lib to fake to create fake superheros and return to us.

<div id='id-prerequisite'/>
## Prerequisite
We require 
- Java (11 or higer)
- Maven /Gradle ( For this article we are using maven)

<div id='id-approach'/>
## Approach

<div id='id-approach0'/>
### Creating sample project of micronaut
There are several ways to start the new project , you can use the launcher website, or initializer using cmd  we will be using launcher website for this article.  Visit  [ https://micronaut.io/launch/](https://micronaut.io/launch/) . 
We are using maven as build tool and java 16 as jdk version. It depends on you if you want to use other tools and jdk version.
We have also changed the application name as super-heroes and base package as ```com.codingsaint```

![Screenshot from 2021-08-19 12-09-16.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1629355198708/nkR9KfMgz.png)

Click on generate the project. It will download the zip version on your local maching. Unzip the file and open in folder / pom in your favorite IDE. I am using intelliJ for it.


<div id='id-approach1'/>
### Running the project
To run the project , you can simply open the terminal and type 
```./mvnw mn:run```
This will download the dependency and start the project on 8080 . 

![Screenshot from 2021-08-19 12-17-49.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1629355690722/xCfOMfj3g.png)

You can check http://localhost:8080

<div id='id-approach2'/>
### Adding dependency for java faker (Optional )
Java Faker lib will help us to create fake super hero names. The goal is to create 10 fake super heros when we hit ```/super-heros``` endpoint. 
Add following dependency in pom file. You will notice the terminal where you started the project will automatically download dependency , if not present , and start the application as well smoothly for a great developer experience. 
```
<dependency>
      <groupId>com.github.javafaker</groupId>
      <artifactId>javafaker</artifactId>
      <version>1.0.2</version>
</dependency>
```
<div id='id-approach3'/>
### Creating a sample endpoint for heroes
Create a SuperHeroController.java class with annotation ```@Controller ``` , We will also add use our faker lib to create fake super heroes for us. 
Below is the code for same
```
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

```
Hit http://localhost:8080/super-heroes  , you will get super charged super heroes with their world saving powers as a response. 
```
[
"General Storm Boy - Physical Anomaly",
"Magnificent Rachel Pirzad Eyes - Atmokinesis",
"Doctor Hawk Lord - Biokinesis",
"Magnificent Ink I - Toxikinesis",
"Arclight - Atmokinesis",
"Sobek - Empathy",
"Isis Dragon - Telekinesis",
"Cyborg Quantum - Levitation",
"Captain Hawk Man - Hypnokinesis",
"Mr Titan - Cloaking"
]
```
As Faker magic , you will generate new super heros every time you want.  

<div id='id-conclusion'/>
## Conclusion
It is super easy to create a simple REST project with micronaut. While there are other great frameworks who are equally good, ```micronaut``` seems exciting as well . It is now backed by oracle so we can be optimistic to see smooth progress in the framework as well.


```
In our series we have created an application which can call superheroes to save the world. But every time we call the superheroes we generate new ones on the fly using Faker , it's good for show of but we need to create real superheroes who can live for longer that a HTTP call.
If you have not followed our tutorials yet , you can look them

- [Micronaut : The starting your first project](https://blog.codingsaint.com/micronaut-the-starting-your-first-project)
- [Creating native image with micronaut and graalvm](https://blog.codingsaint.com/creating-native-image-with-micronaut-and-graalvm)

Source code of this tutorial:  [ https://github.com/CODINGSAINT/super-heroes/tree/02MICRONAUT_DATABASE](https://github.com/CODINGSAINT/super-heroes/tree/02MICRONAUT_DATABASE)

To Save our superheroes we need a home for them to live. We will give them a home and store them. We give them a base to live in called **H2 Database**.
Let us start adding the dependency for H2 database and the JDBC .
Add following in your pom file
```
<dependency> 
    <groupId>io.micronaut.data</groupId>
    <artifactId>micronaut-data-jdbc</artifactId>
    <scope>compile</scope>
</dependency>
<dependency> 
    <groupId>io.micronaut.sql</groupId>
    <artifactId>micronaut-jdbc-hikari</artifactId>
    <scope>compile</scope>
</dependency>
<dependency> 
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

Since we have to process annotations at compile time , we will add following inside ```annotationProcessorPaths``` tag.

```
<path> 
    <groupId>io.micronaut.data</groupId>
    <artifactId>micronaut-data-processor</artifactId>
   <version>${micronaut.version}</version>
</path>
```

Let us create a ```dataSource``` . We will add following properties for data source

```
datasources:
  default:
    url: ${JDBC_URL:`jdbc:h2:mem:default;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`}
    username: ${JDBC_USER:sa}
    password: ${JDBC_PASSWORD:""}
    driverClassName: ${JDBC_DRIVER:org.h2.Driver}
```
Now we have added dataSource . let us create a Data object for defining Superheroes and save them to database.

```
@MappedEntity
public record Superhero(@Id @GeneratedValue @Nullable Long id,
                        String name,
                        String prefix,
                        String suffix,
                        String power) {
}

```
We will not create Repository to facilitate the database operation. We will create an ```interface``` named ```SuperheroRepository``` and extend ```PageableRepository`` from micronaut.

```
@JdbcRepository(dialect = Dialect.H2)
public interface SuperheroRepository extends PageableRepository<Superhero,Long> {
```

Now we can inject this ```SuperheroRepository``` to our ```SuperHeroController```. We will also add ```ExecuteOn``` annotation over controller .

```
@Controller
@ExecuteOn(TaskExecutors.IO)
public class SuperHeroController {
    private static final Logger logger = LoggerFactory.getLogger(SuperHeroController.class);
    protected SuperheroRepository superheroRepository;

    SuperHeroController(SuperheroRepository superheroRepository) {
        this.superheroRepository = superheroRepository;
    }
}
```
Inside the controller we will add the method to save, update , retrieve and delete.

### Saving the superheroes
Below is a sample code for saving the super hero along with a private utility method URI to generate URLs

```
 @Post("superhero")
    public HttpResponse<Superhero> save(@Valid Superhero superhero) {
        logger.info("Saving new Superhero {}", superhero);
        var superHero = superheroRepository.save(superhero);
        return HttpResponse.created(superHero)
                .headers(headers -> headers.location(URI("/superhero/" + "" + superHero.id())));

    }
private URI URI(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
            return uri;
        } catch (URISyntaxException e) {
            logger.error("Error while creating URL {}",e);
        }
        return uri;
    }
```

### Retrieving Superheroes
Below is a sample code to retrieve superheroes by id.
```
@Get("superhero/{id}")
    public Optional<Superhero> get(Long id) {
        return superheroRepository.findById(id);
    }

```

### Updating Superhero

```
@Put("superhero")
    public HttpResponse update(@Body @Valid Superhero superhero) {
        superheroRepository.update(superhero);
        return HttpResponse
                .noContent()
                .headers(headers ->
                        headers.location(URI("/superhero/" + "" + superhero.id())));
    }
```

### Deleting Superhero by id
```
 @Delete("superhero/{id}")
    @Status(HttpStatus.NO_CONTENT)
    public void delete(Long id) {
        superheroRepository.deleteById(id);
    }
```
In this article we saw how to perform CRUD operations with micronaut .

---
If you like my articles you can support me by sharing the articles with your friends, twitter, facebook or any other social media. You can also buy me a  coffee
[![output-onlinepngtools.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1629455712688/16nGo0bof.png)]( https://buymeacoffee.com/kumarpallav) 



