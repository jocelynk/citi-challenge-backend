package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import play.data.validation.Constraints;

/**
 * Created by sasinda on 10/15/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class User extends BaseModel{

    private String userId;
    @Constraints.Required
    private String userName;
    @Constraints.Required
    private String email;
    @Constraints.Required
    private String password;

    private boolean passiveAuth;

    private Integer mobile;


    @Override
    public void setId(String id) {
        super.setId(id);
        this.userId=id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isPassiveAuth() {
        return passiveAuth;
    }

    public void setPassiveAuth(boolean passiveAuth) {
        this.passiveAuth = passiveAuth;
    }
}
