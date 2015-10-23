package controllers;


import dto.DevicesDto;
import exception.RESTException;
import model.Device;
import model.Login;
import model.User;
import org.bson.Document;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import service.DeviceService;
import service.UserService;

import static play.data.Form.form;

/**
 * Created by sasinda on 9/30/15.
 */
public class API extends Controller {

    static DeviceService devService = new DeviceService();
    static UserService userService=new UserService();


    public static Result createDevices() {
        DevicesDto devs = extract(DevicesDto.class);
        User user=new User();
        user.setPassiveAuth(true);
        userService.updateUserById(devs.getUserId(),user);
        Document doc = devService.createDevices(devs.getDevices());
        return getResponse(doc, "error creating devices");
    }

    public static Result getUser(String userName){
        User user=userService.getUser(userName);
        return ok(Json.toJson(user));
    }



    public static Result login() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        String userName = loginForm.get().userName;
        String pass = loginForm.get().password;

        User user = userService.authenticate(userName, pass);
        //if success
        session().clear();
        session("userName", user.getUserName());
        response().setCookie("userName", user.getUserName());
        user.setPassword(null);
        return ok(Json.toJson(user));
    }

    public static Result logout() {
        session().clear();
        response().discardCookie("user-name");
        return redirect(
            routes.Application.index()
        );
    }

    private static <T> T extract(Class<T> modelType) {
        T model = null;
        try {
            model = Json.fromJson(request().body().asJson(), modelType);
        } catch (IllegalStateException ex) {
            throw new RESTException(ex.getMessage());
        }
        return model;
    }

    private static <T> T extract(Json json, Class<T> modelType) {
        T model = null;
        try {
            model = Json.fromJson(request().body().asJson(), modelType);
        } catch (IllegalStateException ex) {
            throw new RESTException(ex.getMessage());
        }
        return model;
    }


    private static Result getResponse(Document doc, String errorMsg) {
        if (doc != null) {
            String id = doc.get("_id").toString();
            if(id==null){
                throw new RESTException("error creating device");
            }
            doc.put("id",id);
            doc.remove("_id");
            return  ok(Json.toJson(doc));
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


    public static Result getDevice() {
        return play.mvc.Results.TODO;
    }


}
