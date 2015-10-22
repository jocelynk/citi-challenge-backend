package controllers;


import exception.RESTException;
import model.Device;
import model.User;
import org.bson.Document;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.DeviceService;

import static play.data.Form.form;

/**
 * Created by sasinda on 9/30/15.
 */
public class API extends Controller {

    DeviceService devService = new DeviceService();


    public Result createDevice() {
        User u = extract(User.class);
        Device d = validateForModel(Device.class);
        Document doc = devService.createDevice(d);
        return getResponse(doc, "error");
    }

    private <T> T extract(Class<T> modelType) {
        T model = null;
        try {
            model = Json.fromJson(request().body().asJson(), modelType);
        } catch (IllegalStateException ex) {
            throw new RESTException(ex.getMessage());
        }
        return model;
    }


    private Result getResponse(Document doc, String errorMsg) {
        if (doc != null) {
            String id = doc.get("_id").toString();
            if(id==null){
                throw new RESTException("error creating device");
            }
            return  ok(Json.newObject().put("id", id));
        } else {
            return badRequest(errorMsg);
        }
    }


    private static <T> T validateForModel(Class<T> modelType) {
        T model = null;
        try {
            model = form(modelType).bindFromRequest().get();
        } catch (IllegalStateException ex) {
            throw new RESTException(ex.getMessage());
        }
        return model;
    }


}
