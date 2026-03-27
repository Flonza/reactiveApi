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
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        String mongoUri = dotenv.get("MONGO_URI", System.getenv("MONGO_URI"));
        return MongoClients.create(mongoUri);
    }

    @Override
    protected String getDatabaseName() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        return dotenv.get("MONGODB_DB", System.getenv("MONGODB_DB"));
    }
}
