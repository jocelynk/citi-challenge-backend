package dto;

/**
 * Created by sasinda on 10/23/15.
 */
public class Message {
    //Message identifiers
    public String event;
    public String type;
    public String userId;
    public String username;

    //device specific
    public String deviceId;
    public DevTypes devType; //beacon, master-phone
    public String bluetoothAddress;
    public String status;
    public int proximity;
    public String wifiSSID;
    public String ipAddress;

    //message content
    public String content;

    public enum DevTypes {
        BEACON,
        MASTER_PHONE
    }
}
