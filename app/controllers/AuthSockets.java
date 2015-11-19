package controllers;

import dto.Message;
import dto.Message.Event;
import model.AuthConfidence;
import model.Device;
import org.apache.commons.collections.map.HashedMap;
import play.libs.F;
import play.mvc.WebSocket;
import service.DeviceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static dto.Message.ActionType.PROMPT;
import static dto.Message.Event.*;

/**
 * Created by sasinda on 11/5/15.
 */
public class AuthSockets {

    DeviceService devService = new DeviceService();
    // UserName -> [<devId, con> <devid2,con>]
    static Map<String, List<Connection>> connetions = new HashedMap();
    // username -> Conf score
    static Map<String, AuthConfidence> scores = new HashedMap();

    public void onMessage(Message message, WebSocket.In<String> in, WebSocket.Out<String> out) {
        //to figure out the user for the connection
        String conId = "browser";
        //figure out The master device sending the message
        if (message.getMasterId() != null) {
            Device master = devService.getDevice(message.getMasterId());
            if (message.getUsername() == null) {
                message.setUsername(master.username);
            }
            conId = master.deviceId;
        }
        // get the target device for the message
        if (message.getDeviceId() != null) {
            Device device = devService.getDevice(message.getDeviceId());
            message.setOrigDevice(device);
        }

        //The message is for a known user, known event
        Event event = message.getEvent();
        if (message.getUsername() != null && event != null) {
            switch (event) {
                case OPEN:
                    onOpen(message, in, out, conId);
                    break;
                case LOGIN_INIT:
                    onLoginInit(message);
                    break;
                case LOGIN_DEVICES:
                    onDeviceDetect(message);
                    break;
                case LOGIN_ACTION_CONFIRMED:
                    onActionConfirm(message);
                    break;
            }
        }

    }


    private void onOpen(Message message, WebSocket.In<String> in, WebSocket.Out<String> out, String conId) {
        List<Connection> list = connetions.get(message.getUsername());
        if (list == null) {
            list = new ArrayList<>();
            connetions.put(message.getUsername(), list);
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

    /*
     * reset score to 0
     * send master devices to initiate auth procedure
     * @param message
     * @param connectionList
     */
    private void onLoginInit(Message message) {
        AuthConfidence authConfidence = scores.get(message.getUsername());
        scores.put(message.getUsername(), new AuthConfidence());
        Message mout = new Message(LOGIN_INIT).setDeviceId("browser");
        broadcast(message, mout);
    }

    private void onDeviceDetect(Message message) {
        AuthConfidence authConfidence = scores.get(message.getUsername());
        if (authConfidence != null) {
            //calc score
            authConfidence.addMessege(message);
            //reply score update
            Message moutUCS = new Message(UPDATE_CONF_SCORE)
                    .setScore(authConfidence.getConfidenceScore())
                    .setSubScores(authConfidence.getSubScores());


            //if action required
            Message moutLAR = null;
            if (authConfidence.isActionRequired() && !authConfidence.isActionRequested()) {
                moutLAR = new Message(LOGIN_ACTION_REQUIRED)
                        .setActionType(PROMPT);
                authConfidence.setActionRequested(true);
            }
            broadcast(message, moutUCS, moutLAR);
        }
    }

    private void onActionConfirm(Message message) {
        AuthConfidence authConfidence = scores.get(message.getUsername());
        if (authConfidence != null) {
            authConfidence.confirmAction();
            Message mout = new Message(UPDATE_CONF_SCORE)
                    .setScore(authConfidence.getConfidenceScore())
                    .setSubScores(authConfidence.getSubScores())
                    .setAuthSuccess(true);
            broadcast(message, mout);
        }
    }

    //get set of conns to broadcast this message
    private List<Connection> getBroadcastList(Message message) {
        return null;
    }

    /**
     * @param in  original message for which the reply is broadcast
     * @param out reply message to be broadcasted.
     */
    private void broadcast(Message in, Message out) {
        List<Connection> conList = connetions.get(in.getUsername());
        if (conList != null) {
            for (Connection connection : conList) {
                System.out.println("send " + out.getEvent() + ": " + connection.conId);
                connection.out.write(out.stringify());
            }
        }

    }

    private void broadcast(Message in, Message... mouts) {
        List<Connection> conList = connetions.get(in.getUsername());
        if (conList != null) {
            for (Connection connection : conList) {
                for (Message mo : mouts) {
                    if (mo != null) {
                        System.out.println("send " + mo.getEvent() + ": " + connection.conId);
                        connection.out.write(mo.stringify());
                    }
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
