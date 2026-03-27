package com.accenture.test.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
public class MongoReactiveConfig extends AbstractReactiveMongoConfiguration {

    private final Dotenv dotenv = Dotenv.load();

    @Override
    public MongoClient reactiveMongoClient() {
        String uri = dotenv.get("MONGODB_URI");
        return MongoClients.create(uri);
    }

    @Override
    protected String getDatabaseName() {
        return dotenv.get("MONGODB_DB");
    }
}
