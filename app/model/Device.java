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
    @Constraints.Required
    public String userId;
}
