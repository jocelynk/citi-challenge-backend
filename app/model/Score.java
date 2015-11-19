package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by sasinda on 11/19/15.
 */
public class Score {
    private String deviceId;
    private double value;
    private Type scoreType;
    private double proximity;

    public Score(String deviceId, double value, Type scoreType, double proximity) {
        this.setValue(value);
        this.setDeviceId(deviceId);
        this.setProximity(proximity);
        this.setScoreType(scoreType);
    }

    public Score(String deviceId, double value, Type scoreType) {
        this.setDeviceId(deviceId);
        this.setValue(value);
        this.setScoreType(scoreType);
    }
    @JsonIgnore
    public String getKey() {
        return getDeviceId() + "_" + getScoreType();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Type getScoreType() {
        return scoreType;
    }

    public void setScoreType(Type scoreType) {
        this.scoreType = scoreType;
    }

    public double getProximity() {
        return proximity;
    }

    public void setProximity(double proximity) {
        this.proximity = proximity;
    }

    public enum Type {
        ACTIVE,
        PROX,
        WIFI_SSID,
        IP_ADDRESS,
        BT_ADDRESS;
    }

    @Override
    public String toString() {
        return "Score{" +
                "deviceId='" + getDeviceId() + '\'' +
                ", value=" + getValue() +
                ", scoreType=" + getScoreType() +
                ", proximity=" + getProximity() +
                '}';
    }
}
