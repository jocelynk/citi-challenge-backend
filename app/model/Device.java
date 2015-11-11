package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import play.data.validation.Constraints;

/**
 * Created by sasinda on 10/22/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Device extends BaseModel{

    @Constraints.Required
    public String deviceId;
    //beacon, smart phone
//  @Constraints.Required
    public String type;
    //apple iphone 6s, glaxy S5 etc
    public String model;
    //wether its a master device
    public boolean isMaster;

    public String name;
    public String bluetoothAddress;
    //TODO: we need a list of reg ssids, with recency and etc
    public String wifiSSID;
    public String ipAddress;

    public User user;
    //just for demo
    public String username;

    public boolean isMaster() {
        return isMaster;
    }

    public void setIsMaster(boolean isMaster) {
        this.isMaster = isMaster;
    }
}
