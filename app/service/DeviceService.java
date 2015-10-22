package service;

import model.Device;
import org.bson.Document;

/**
 * Created by sasinda on 10/22/15.
 */
public class DeviceService extends DataService{

    public Document createDevice(Device d) {
        Document doc=asDocument(d);
        db.getCollection("Device").insertOne(doc);
        return doc;
    }
}
