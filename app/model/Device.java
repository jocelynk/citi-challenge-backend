package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import play.data.validation.Constraints;

/**
 * Created by sasinda on 10/22/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Device {
    public String id;
    @Constraints.Required
    public String deviceId;
    //beacon, smart phone
//  @Constraints.Required
    public String type;
    //apple iphone 6s, glaxy S5 etc
    public String model;

    public String name;

    public User user;

}
