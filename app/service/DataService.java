package service;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;
import play.libs.Json;

/**
 * Created by sasinda on 10/22/15.
 */
public class DataService {
    MongoClient mongoClient = new MongoClient("localhost");
    MongoDatabase db = mongoClient.getDatabase("citiauth");



    public static Document asDocument(Object o) {
        DBObject adv = (DBObject) JSON.parse(Json.toJson(o).toString());
        return new Document(adv.toMap());
    }
}
