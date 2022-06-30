package database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class Database {
    private MongoDatabase mongoDatabase;

    public Database(String databaseName) {
        MongoClient mongoClient = new MongoClient();
        mongoDatabase = mongoClient.getDatabase(databaseName);
    }

    public MongoDatabase getDatabase() {
        return mongoDatabase;
    }
}
