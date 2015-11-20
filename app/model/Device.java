package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import enums.DeviceType;
import play.data.validation.Constraints;

/**
 * Created by sasinda on 10/22/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Device extends BaseModel{

    @Constraints.Required
    private String deviceId;
    //beacon, smart phone
//  @Constraints.Required
    private DeviceType type;
    //apple iphone 6s, glaxy S5 etc
    private String model;
    //wether its a master device
    private boolean isMaster;

    private String name;
    private String bluetoothAddress;
    //TODO: we need a list of reg ssids, with recency and etc
    private String wifiSSID;
    private String ipAddress;
    private Device pairedDevices;

    private User user;
    //just for demo
    private String username;


    public Device() {
    }

    public Device(String deviceId, DeviceType type) {
        this.deviceId = deviceId;
        this.type = type;
    }

    public Device clear(){
        Device device=new Device(deviceId, type);
        device.setIsMaster(isMaster);
        return device;
    }



    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }

    public String getWifiSSID() {
        return wifiSSID;
    }

    public void setWifiSSID(String wifiSSID) {
        this.wifiSSID = wifiSSID;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Device getPairedDevices() {
        return pairedDevices;
    }

    public void setPairedDevices(Device pairedDevices) {
        this.pairedDevices = pairedDevices;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setIsMaster(boolean isMaster) {
        this.isMaster = isMaster;
    }
}
