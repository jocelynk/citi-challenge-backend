package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import model.BaseModel;
import org.bson.Document;
import play.Play;
import play.libs.Json;

import java.io.IOException;

/**
 * Created by sasinda on 10/22/15.
 */
public class DataService {


    final static String dburi= Play.application().configuration().getString("mongodb.uri");
    final static String dbName= Play.application().configuration().getString("mongodb.name");
    final static MongoClientURI uri = new MongoClientURI(dburi);

    //connection
    final static public MongoClient mongoClient = new MongoClient(uri);
    final static MongoDatabase db = mongoClient.getDatabase(dbName);

    private static ObjectMapper mapper =new ObjectMapper();


    public static <T extends BaseModel>  T as(Class<T> as , Document doc){
        if(doc==null) return null;
        try {
            T t = mapper.readValue(doc.toJson(), as);
            t.setId(doc.get("_id").toString());
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Document asDocument(Object o) {
        DBObject adv = (DBObject) JSON.parse(Json.toJson(o).toString());
        return new Document(adv.toMap());
    }

    public static BasicDBObject asDBObject(Object o) {
        BasicDBObject adv = (BasicDBObject) JSON.parse(Json.toJson(o).toString());
        return adv;
    }


}
