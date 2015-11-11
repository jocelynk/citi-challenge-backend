package controllers;

import dto.Message;
import org.apache.commons.collections.map.HashedMap;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.index;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application extends Controller {

    private static PassiveAuth auth=new PassiveAuth();

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static WebSocket<String> ws() {
        return new WebSocket<String>() {
            // Called when the Websocket Handshake is done.
            public void onReady(In<String> in, Out<String> out) {
                // For each event received on the socket,
                in.onMessage(new F.Callback<String>() {
                    public void invoke(String event) {
                        // Log events to the console
                        Message message = Json.fromJson(Json.parse(event), Message.class);
                        auth.onMessage(message,in,out);
                    }
                });

                // When the socket is closed.
                in.onClose(new F.Callback0() {
                    public void invoke() {
                        System.out.println("closed");
                    }
                });
                // Send a single 'Hello!' message
                out.write("Hello!");

            }

        };

    }




}
