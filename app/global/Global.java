package global;

import exception.AuthException;
import exception.RESTException;
import play.*;
import play.mvc.*;
import play.mvc.Http.*;
import play.libs.F.*;
import play.GlobalSettings;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.internalServerError;
import static play.mvc.Results.seeOther;

/**
 * Created by sasinda on 10/16/15.
 */
public class Global extends GlobalSettings {

//    public Promise<Result> onError(RequestHeader request, Throwable t) {
//        if(t instanceof AuthException){
//            return Promise.<Result>pure(badRequest(t.toString()));
//        }else if(t instanceof RESTException){
//            return Promise.<Result>pure(badRequest(t.toString()));
//        }
//        System.out.println(t);
//        return super.onError(request,t);
////        return Promise.<Result>pure(badRequest(t.toString()));
//    }

    public Promise<Result> onBadRequest(RequestHeader request, String error) {
        return Promise.<Result>pure(badRequest("Don't try to hack the URI!"));
    }
}