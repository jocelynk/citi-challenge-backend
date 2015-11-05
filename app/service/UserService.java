package service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import exception.AuthException;
import model.User;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by sasinda on 10/22/15.
 */
public class UserService extends DataService {

    private final MongoCollection<Document> collection = db.getCollection("User");

    /**
     * return error code if fail
     *
     * @param userName
     * @param password
     * @return
     */
    public User authenticate(String userName, String password) {
        Document doc = collection.find(eq("username", userName)).first();
        User user = as(User.class, doc);
        if (user == null) {
            throw new AuthException("user not found");
        } else if (user.getPassword().equals(password)) {
            return user;
        } else {
            throw new AuthException("password incorrect");
        }
    }

    public Document createUser(User u) {
        Document doc = asDocument(u);
        collection.insertOne(doc);

        return doc;
    }

    public User getUser(String userName) {
        return as(User.class, db.getCollection("User").find(eq("username", userName)).first());
    }

    public User getUserById(String id){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        Document doc = collection.find(query).first();
        return as(User.class, doc);
    }

    public boolean updateUserById(String id, User user){
//        BasicDBObject query = new BasicDBObject();
//        query.put("_id", new ObjectId(id));
        Document userDoc = asDocument(user);
        long modifiedCount = collection.updateOne(new Document("userId", id), new Document("$set",userDoc)).getModifiedCount();
        return modifiedCount>0;
    }

}
