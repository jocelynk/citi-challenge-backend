package dto;

import model.Device;

import java.util.List;

/**
 * Created by sasinda on 10/23/15.
 */
public class DevicesDto {
    private String userId;
    private List<Device> devices;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}
