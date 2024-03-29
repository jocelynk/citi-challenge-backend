package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import model.Device;

import java.util.List;

/**
 * Created by sasinda on 10/23/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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
