package org.example.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import lombok.SneakyThrows;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    /**
     * Получает название коллекций
     * @param database
     * @return
     */
    public static List <String> getCollectionsListString(MongoDatabase database){
        return castMongoIterableToString(database.listCollectionNames());
    }


    /**
     * Получает название БД>

     * @return
     */
    public static List <String> getDBListString(MongoClient connector){
        return castMongoIterableToString(connector.listDatabaseNames());
    }

    private static List<String> castMongoIterableToString( MongoIterable<String> mongoList){
        ArrayList <String> list = new ArrayList<>();
        for (String obj : mongoList) {
            list.add(obj);
        }
        return list;
    }

    public static BasicDBObject createProjectionByFields(String... fields){
        BasicDBObject projection = new BasicDBObject("_id", 0);
        for (String fieldToShow : fields) {
            projection.append(fieldToShow, 1);
        }
        return projection;
    }

    public static void logOperationResult(FindIterable<Document> result) {
        StringBuilder builder = new StringBuilder();
        for (Document doc : result) {
            builder.append(doc.toJson()).append("\n");
        }
        String message = builder.toString().trim();
        log.info(message);
    }

    public static void logOperationResult(AggregateIterable<Document> result) {
        StringBuilder builder = new StringBuilder();
        for (Document doc : result) {
            builder.append(doc.toJson()).append("\n");
        }
        String message = builder.toString().trim();
        log.info(message);
    }

    public static void logOperationResult(FindIterable<Document> result, String... showFields) {
        StringBuilder sb = new StringBuilder();
        for (Document doc : result) {
            sb.append("{");
            for (String field : showFields) {
                if (doc.containsKey(field)) {
                    sb.append("\"").append(field).append("\": ").append(doc.get(field)).append(", ");
                }
            }
            if (sb.length() > 1) {
                sb.setLength(sb.length() - 2);
            }
            sb.append("}\n");
        }
        String message = sb.toString().trim();
        log.info(message);
    }

    @SneakyThrows
    public static Document getDocFromFile(String fileName){
        ClassLoader classLoader = Utils.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
        String jsonContent = new String(Files.readAllBytes(file.toPath()));
        JSONObject jsonObject = new JSONObject(jsonContent);
        return Document.parse(jsonObject.toString());
    }


    @SneakyThrows
    public static List<Document> getDocsFromFile(String fileName){
        ClassLoader classLoader = Utils.class.getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
        String jsonContent = new String(Files.readAllBytes(file.toPath()));
        JSONArray jsonArray = new JSONArray(jsonContent);
        List<Document> documents = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Document document = Document.parse(jsonObject.toString());
            documents.add(document);
        }
        return documents;
    }
}
