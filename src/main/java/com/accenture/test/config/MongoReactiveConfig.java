package com.accenture.test.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
public class MongoReactiveConfig extends AbstractReactiveMongoConfiguration {

    private final Dotenv dotenv;

    public MongoReactiveConfig() {
        this.dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
    }

    @Override
    public MongoClient reactiveMongoClient() {
        String mongoUri = System.getenv("MONGO_URI");
        if (mongoUri == null || mongoUri.isEmpty()) {
            mongoUri = dotenv.get("MONGO_URI");
        }
        return MongoClients.create(mongoUri);
    }

    @Override
    protected String getDatabaseName() {
        String database = System.getenv("MONGODB_DB");
        if (database == null || database.isEmpty()) {
            database = dotenv.get("MONGODB_DB");
        }
        return database;
    }
}