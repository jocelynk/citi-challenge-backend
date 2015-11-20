package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import enums.DeviceType;
import model.Device;
import model.Score;
import service.Util;

import java.util.List;

/**
 * Created by sasinda on 10/23/15.
 * AIX messsage Auth Information Exchange
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {

    //Event requird
    private Event event;
    //one of the following is required
    private String userId;
    private String username;
    //The id of the master device sending this.
    private String masterId;

    //device specific
    private String deviceId;
    private DeviceType deviceType; //BEACON, SMART_PHONE
    private String deviceName;
    private String bluetoothAddress;
    private String status;
    private int proximity;
    private String wifiSSID;
    private String ipAddress;
    private String content;

    private int score;
    private boolean authSuccess;
    private List<Score> subScores;

    private ActionType actionType;

    private Device origDevice;


    public Message() {
    }

    public Message(Event event) {
        this.setEvent(event);
    }






    public enum Event {
        OPEN,
        LOGIN_INIT,
        LOGIN_DEVICES,
        LOGIN_ACTION_REQUIRED,
        LOGIN_ACTION_CONFIRMED,
        UPDATE_CONF_SCORE;
    }

    public enum ActionType{
        SHAKE,
        PROMPT
    }

    public String stringify() {
        return Util.stringigy(this);
    }

    @Override
    public String toString() {
        return stringify();
    }


    /**
     * ########### geters setters #################
     */

    /**
     * HEADER
     * Message identifiers
     */
    public Event getEvent() {
        return event;
    }

    public Message setEvent(Event event) {
        this.event = event;
        return this;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public Message setActionType(ActionType actionType) {
        this.actionType = actionType;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public Message setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Message setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getMasterId() {
        return masterId;
    }

    public Message setMasterId(String masterId) {
        this.masterId = masterId;
        return this;
    }

    /**
     * BODY
     * Message Payload
     */
    public String getDeviceId() {
        return deviceId;
    }

    public Message setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public Message setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public Message setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public Message setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Message setStatus(String status) {
        this.status = status;
        return this;
    }

    public int getProximity() {
        return proximity;
    }

    public Message setProximity(int proximity) {
        this.proximity = proximity;
        return this;
    }

    public String getWifiSSID() {
        return wifiSSID;
    }

    public Message setWifiSSID(String wifiSSID) {
        this.wifiSSID = wifiSSID;
        return this;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Message setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Message setContent(String content) {
        this.content = content;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Message setScore(int score) {
        this.score = score;
        return this;
    }

    public List<Score> getSubScores() {
        return subScores;
    }

    public Message setSubScores(List<Score> subScores) {
        this.subScores = subScores;
        return this;
    }

    /**
     * Piggyback data for calculations, on backend
     */
    public Device getOrigDevice() {
        return origDevice;
    }

    public Message setOrigDevice(Device origDevice) {
        this.origDevice = origDevice;
        return this;
    }

    public boolean isAuthSuccess() {
        return authSuccess;
    }

    public Message setAuthSuccess(boolean authSuccess) {
        this.authSuccess = authSuccess;
        return this;
    }



}
