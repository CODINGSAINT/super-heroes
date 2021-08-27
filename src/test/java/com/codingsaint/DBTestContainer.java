package com.codingsaint;

import org.testcontainers.containers.PostgreSQLContainer;

public class DBTestContainer extends PostgreSQLContainer<DBTestContainer> {
    private static DBTestContainer postGresTestContainer;



    public void start() {
        if (postGresTestContainer==null) {
            postGresTestContainer = new DBTestContainer();
            postGresTestContainer.setDockerImageName("postgres:12");
            System.setProperty("datasources.default.url", postGresTestContainer.getJdbcUrl());
            System.setProperty("datasources.default.username", postGresTestContainer.getUsername());
            System.setProperty("datasources.default.password", postGresTestContainer.getPassword());

        }

    }

    public void stop() {
        if (postGresTestContainer!=null)
            postGresTestContainer.stop();
    }


}
