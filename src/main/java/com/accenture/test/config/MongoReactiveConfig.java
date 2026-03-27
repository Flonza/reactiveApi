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
        Dotenv temp = null;
        try {
            temp = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
        } catch (Exception e) {
            System.out.println("No .env file found, using environment variables only");
        }
        this.dotenv = temp;
    }

    @Override
    public MongoClient reactiveMongoClient() {
        String mongoUri = System.getenv("MONGO_URI");
        if ((mongoUri == null || mongoUri.isEmpty()) && dotenv != null) {
            mongoUri = dotenv.get("MONGO_URI");
        }
        if (mongoUri == null || mongoUri.isEmpty()) {
            throw new IllegalStateException("MONGO_URI environment variable is not set");
        }
        return MongoClients.create(mongoUri);
    }

    @Override
    protected String getDatabaseName() {
        String database = System.getenv("MONGODB_DB");
        if ((database == null || database.isEmpty()) && dotenv != null) {
            database = dotenv.get("MONGODB_DB");
        }
        if (database == null || database.isEmpty()) {
            throw new IllegalStateException("MONGODB_DB environment variable is not set");
        }
        return database;
    }
}