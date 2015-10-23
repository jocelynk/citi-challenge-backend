package controllers;

import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }


    public static WebSocket<String> ws() {
        return new WebSocket<String>() {

            // Called when the Websocket Handshake is done.
            public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {

                // For each event received on the socket,
                in.onMessage(new F.Callback<String>() {
                    public void invoke(String event) {
                     // Log events to the console
                        System.out.println(event);
                    }
                });

                // When the socket is closed.
                in.onClose(new F.Callback0() {
                    public void invoke() {

                        System.out.println("Disconnected");

                    }
                });
                System.out.println("sending hello!");
                // Send a single 'Hello!' message
                out.write("Hello!");

            }

        };
    }

}
