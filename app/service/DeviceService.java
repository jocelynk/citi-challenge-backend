package service;

import model.Device;
import org.bson.Document;

import java.util.List;

/**
 * Created by sasinda on 10/22/15.
 */
public class DeviceService extends DataService{


    public Document createDevice(Device d) {
        Document doc=asDocument(d);
        db.getCollection("Device").insertOne(doc);
        return doc;
    }

    public Document createDevices(List<Device> devices) {
        return null;
    }
}
