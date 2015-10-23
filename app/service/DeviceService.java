package service;

import model.Device;
import org.bson.Document;

import java.sql.Array;
import java.util.ArrayList;
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
        List<Document> list=new ArrayList<Document>();
        Document deviceList=new Document("devices", list );
        deviceList.put("_id", "hack");
        for (Device device : devices) {
            Document device1 = createDevice(device);
            list.add(device1);
        }

        return deviceList;
    }
}
