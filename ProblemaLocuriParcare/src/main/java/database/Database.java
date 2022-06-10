package database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class Database {
    private static Database instance;
    private MongoDatabase mongoDatabase;
    private Database() {
        MongoClient mongoClient = new MongoClient();
        mongoDatabase = mongoClient.getDatabase("parkingLotDB");
    }

    public static Database getInstance() {
        if(instance == null)
            return new Database();
        return instance;
    }

    public MongoDatabase getParkingLotDB() {
        return mongoDatabase;
    }
}
