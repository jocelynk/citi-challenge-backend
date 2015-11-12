package controllers;

import dto.Message;
import model.AuthConfidence;
import model.Device;
import org.apache.commons.collections.map.HashedMap;
import play.api.libs.json.Json;
import play.libs.F;
import play.mvc.WebSocket;
import service.DeviceService;

import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sasinda on 11/5/15.
 */
public class PassiveAuth {

    DeviceService devService = new DeviceService();
    static Map<String, List<Connection>> connetions = new HashedMap();
    static Map<String, AuthConfidence> confs = new HashedMap();

    public void onMessage(Message message, WebSocket.In<String> in, WebSocket.Out<String> out) {
        //to figure out the user for the connection
        String conId = "browser";
        if (message.masterId != null) {
            Device master = devService.getDevice(message.masterId);
            if (message.username == null) {
                message.username = master.username;
            }
            conId = master.deviceId;
        }

        if (message.deviceId != null) {
            Device device = devService.getDevice(message.deviceId);
            message.origDevice = device;
        }

        if ("OPEN".equalsIgnoreCase(message.event)) {

            if (message.username != null) {
                List<Connection> list = connetions.get(message.username);
                if (list == null) {
                    list = new ArrayList<>();
                    connetions.put(message.username, list);
                }

                final Connection me = new Connection(conId, in, out);
                final List<Connection> myList = list;
                list.add(me);
                in.onClose(new F.Callback0() {
                    public void invoke() {
                        System.out.println("closed:" + myList.remove(me) + "  :  " + me.conId);

                    }
                });
            }
        } else if ("LOGIN_INIT".equalsIgnoreCase(message.event)) {
            List<Connection> connectionList = connetions.get(message.username);
            if (connectionList != null) {
                for (Connection connection : connectionList) {
                    System.out.println("sending to :" + connection.conId);
                    connection.out.write("{\"event\":\"LOGIN_INIT\", \"deviceId\":\"browser\"}");
                    confs.put(message.username, new AuthConfidence());
                }
            }

        } else if ("LOGIN_DEVICES".equalsIgnoreCase(message.event)) {
            //start calculating score.
            AuthConfidence authConfidence = confs.get(message.username);
            List<Connection> connectionList = connetions.get(message.username);
            if (authConfidence != null && connectionList != null) {
                authConfidence.addMessege(message);
                for (Connection connection : connetions.get(message.username)) {
                    if (authConfidence.actionRequired) {
                        connection.out.write("{\"event\":\"LOGIN_ACTION_REQUIRED\", \"type\":\"SHAKE\"}");
                    }
                    connection.out.write("{\"event\":\"UPDATE_CONF_SCORE\", \"score\":\" " + authConfidence.confidenceScore + "\"}");
                }
            }

        }


    }

    public static class Connection {
        public String conId;
        WebSocket.In<String> in;
        WebSocket.Out<String> out;

        public Connection(String conId, WebSocket.In<String> in, WebSocket.Out<String> out) {
            this.in = in;
            this.out = out;
            this.conId = conId;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Connection) {
                return ((Connection) obj).conId.equals(conId);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return conId.length();
        }
    }

}
