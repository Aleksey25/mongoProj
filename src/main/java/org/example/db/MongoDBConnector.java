package org.example.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import org.example.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MongoDBConnector {
    private static final Logger log = LoggerFactory.getLogger(MongoDBConnector.class);
    @Getter
    private MongoClient mongoClient;
    @Getter
    @Setter
    private MongoDatabase database;

    public MongoDBConnector() {
        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
        this.database = mongoClient.getDatabase("projDB");
        log.info("Connect to DB " + mongoClient.toString() + " - OK" );
    }

    public MongoDatabase setDatabaseByName(String databaseName) {
        this.database = mongoClient.getDatabase("projDB");
        return database;
    }

    public MongoDBConnector(String mongoConfig) {
        this.mongoClient = MongoClients.create(mongoConfig);
    }


    public List<String> getDBList(){
        return Utils.getDBListString(mongoClient);
    }

    public void dropDB(){
        database.drop();
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            log.info("Connect to DB " + mongoClient.toString() + " - Closed" );
        }
    }
}
