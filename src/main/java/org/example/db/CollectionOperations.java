package org.example.db;

import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.example.utils.Utils.*;

public class CollectionOperations{

    private static final Logger log = LoggerFactory.getLogger(CollectionOperations.class);

    private MongoDBConnector connect;
    private MongoCollection<Document> collection;



    public CollectionOperations(MongoDBConnector connect, MongoCollection<Document> collection) {
        this.connect = connect;
        this.collection = collection;
    }

    public CollectionOperations(MongoDBConnector connect) {
        this.connect = connect;
    }


    public MongoCollection<Document> getCollection(String collectionName) {
        return connect.getDatabase().getCollection(collectionName);
    }

    public MongoCollection<Document> createCollection(String collectionName) {
        connect.getDatabase().createCollection(collectionName);
        this.collection = getCollection(collectionName);
        log.info("Created collection [" + collectionName + "]");
        return getCollection(collectionName);
    }

    public void dropCollection(String collectionName) {
        if (getCollectionsListString(connect.getDatabase()).contains(collectionName)){
            connect.getDatabase().getCollection(collectionName).drop();
            log.info("Collection " + collectionName + " dropped");
        }
    }

    public void insertManyFromFile(MongoCollection<Document> collection, String fileName){
        List<Document> documents = getDocsFromFile(fileName);
        collection.insertMany(documents);
        log.info("Inserted " + documents.size() + " documents from file " + fileName);
    }

    public void simpleFind(String field, Object value, String... showFields){
        findByQuery(new BasicDBObject(field, value), showFields);
    }

    public void findByQuery(Bson query, String... showFields) {
        logOperationResult(collection.find(query), showFields);
    }

    public void findByQueryMongoProj(Bson query, String... showFields) {
        logOperationResult(collection.find(query).projection(createProjectionByFields(showFields)));
    }

    public void findByQueryWithoutLog(Bson query) {
        collection.find(query);
    }

    public void replaceFromFile(String field, Object value, String fileName){
        collection.replaceOne(new BasicDBObject(field, value), getDocFromFile(fileName));
    }

    public void updateDocuments(Bson filter, Bson operation){
        UpdateResult result = collection.updateMany(filter, operation);
        log.info("Updated " + result.getModifiedCount() + " documents");
    }

    public void aggregateFromFile(String fileName){
        AggregateIterable<Document> result =  collection.aggregate(getDocsFromFile(fileName));
        logOperationResult(result);
    }

    public void deleteDocuments(Bson filter){
        DeleteResult result = collection.deleteMany(filter);
        log.info("Deleted " + result.getDeletedCount() + " documents");
    }
}
