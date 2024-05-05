package org.example;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.db.CollectionOperations;
import org.example.db.MongoDBConnector;
import org.example.querry.QueryBuilder;
import org.example.querry.QueryCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.example.querry.QueryOperator.*;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final QueryBuilder builder = new QueryBuilder();

    public static void main(String[] args) {
        MongoDBConnector connector = new MongoDBConnector();
        log.info("List of dataBases: " + connector.getDBList().toString());
        CollectionOperations operations = new CollectionOperations(connector);
        try {
            operations.dropCollection("stadiums");
            MongoCollection<Document> collection = operations.createCollection("stadiums");
            operations.insertManyFromFile(collection, "jsons/stadiums.json");

            finds(operations);
            updates(operations);
            agregates(operations);
            deletes(operations);

        } catch (Exception e) {
            log.warn("Error: " + e.getMessage());
        } finally {
            connector.close();
        }
    }

    public static void finds(CollectionOperations operations){
        //Simple find
        operations.simpleFind("name", "Old Trafford", "name", "city");

        //Find new stadiums
        Bson query = builder.addCondition(OR,
                new QueryCondition("buildYear", GT, 2010),
                new QueryCondition("reconstructionYear", GT, 2010)
        ).build();
        operations.findByQueryMongoProj(query, "name", "city", "wasUCLFinal"); // проекция почему-то не возвращает поля в нужном порядке

        //Добавим условие, что не было финала ЛЧ
        query = builder.addCondition(OR,
                        new QueryCondition("buildYear", GT, 2010),
                        new QueryCondition("reconstructionYear", GT, 2010)
                )
                .addCondition(AND,
                        new QueryCondition("wasUCLFinal", false))
                .build();
        operations.findByQuery(query, "name", "city", "wasUCLFinal");
    }


    public static void updates(CollectionOperations operations){
        operations.replaceFromFile("club", "Paris Saint-Germain", "jsons/PSGArena.json");

        operations.updateDocuments(
                builder.addCondition(new QueryCondition("club", "Barcelona")).build(),
                builder.addCondition(SET, new QueryCondition("name", "Spotify")).build()
        );

        operations.updateDocuments(
                builder.addCondition(new QueryCondition("club", "Barcelona")).build(),
                builder.addCondition(INC, new QueryCondition("capacity", 20000)).build()
        ) ;

    }


    public static void agregates(CollectionOperations operations){
        log.info("Количество и средняя вместимость стадионов новее 2000 года");
        operations.agregateFromFile("jsons/agregate.json");
    }

    public static void deletes(CollectionOperations operations){
        operations.deleteDocuments(
                builder.addCondition(REGEX, new QueryCondition("club", "^Manchester")).build()
        );
    }
}