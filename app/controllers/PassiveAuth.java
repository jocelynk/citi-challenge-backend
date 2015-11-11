package controllers;

import dto.Message;
import model.Device;
import org.apache.commons.collections.map.HashedMap;
import play.libs.F;
import play.mvc.WebSocket;
import service.DeviceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sasinda on 11/5/15.
 */
public class PassiveAuth {

    DeviceService devService =new DeviceService();
    static Map<String, List<Connection>> connetions=new HashedMap();

    public void onMessage(Message message, WebSocket.In<String> in, WebSocket.Out<String> out){
        if ("OPEN".equalsIgnoreCase(message.event)) {
            if (message.username == null) {
                Device device = devService.getDevice(message.deviceId);
                message.username=device.username;
            }
            List<Connection> list = connetions.get(message.username);
            if(list==null){
                list=new ArrayList<>();
                connetions.put(message.username, list);
            }
            list.add( new Connection(in,out) );

            in.onClose(new F.Callback0() {
                public void invoke() {
//                    list;
                }
            });
        }else if("LOGIN_INIT".equalsIgnoreCase(message.event)){
//            connetions.get(message.username).out.write("{\"event\":\"LOGIN_INIT\", \"deviceId\":\"browser\"}");

        }

    }

    public static class Connection {
        WebSocket.In<String> in;
        WebSocket.Out<String> out;

        public Connection(WebSocket.In<String> in, WebSocket.Out<String> out) {
            this.in = in;
            this.out = out;
        }
    }

}
