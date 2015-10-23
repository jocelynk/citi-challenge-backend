package model;

import play.data.validation.Constraints;

/**
 * Created by sasinda on 10/15/15.
 */
public class Login {
    @Constraints.Required
    public String userName;
    @Constraints.Required
    public String password;


    public String redirect;
}
