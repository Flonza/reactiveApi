package com.accenture.test.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
public class MongoReactiveConfig extends AbstractReactiveMongoConfiguration {

    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create("mongodb+srv://mendoza_db_user:iS4BXTZmC4z9nKXX@franchise-cluster.udhr2xv.mongodb.net/franchisedb?retryWrites=true&w=majority&appName=franchise-cluster");
    }

    @Override
    protected String getDatabaseName() {
        return "franchisedb";
    }
}
