package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.ConfigException;
import dto.Message;
import org.apache.commons.collections.map.HashedMap;
import play.libs.Akka;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import scala.concurrent.ExecutionContext;
import views.html.index;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application extends Controller {

    public final static ExecutionContext ctx = Akka.system().dispatchers().lookup("akka.my-dispatcher");
    private static ObjectMapper mapper = new ObjectMapper();

    private static PassiveAuth auth = new PassiveAuth();

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
                        ctx.execute( ()-> {
                                    try {
                                        System.out.println(Thread.currentThread().getName()+": "+event);
                                        Message message = mapper.readValue(event, Message.class);
                                        auth.onMessage(message, in, out);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
//                                    return null;
                                }
                        );

                    }
                });
                // When the socket is closed.
                // Send a single 'Hello!' message


            }

        };

    }


}
