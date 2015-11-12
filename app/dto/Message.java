package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import model.Device;

/**
 * Created by sasinda on 10/23/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    //Message identifiers
    public String event;
    public String type;
    public String userId;
    public String username;
    //The id of the master device sending this.
    public String masterId;

    //device specific
    public String deviceId;
    public DevTypes devType; //BEACON, SMART_PHONE
    public String deviceName;
    public String bluetoothAddress;
    public String status;
    public int proximity;
    public String wifiSSID;
    public String ipAddress;

    public Device origDevice;

    //message content
    public String content;

    public enum DevTypes {
        BEACON,
        SMART_PHONE
    }
}
