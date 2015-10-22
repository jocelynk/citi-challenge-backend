package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import play.data.validation.Constraints;

/**
 * Created by sasinda on 10/15/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseModel{

    private String userId;
    @Constraints.Required
    private String name;
    @Constraints.Required
    private String email;
    @Constraints.Required
    private String password;

    private Integer mobile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMobile() {
        return mobile;
    }

    public void setMobile(Integer mobile) {
        this.mobile = mobile;
    }
}
