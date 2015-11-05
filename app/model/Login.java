package model;

import play.data.validation.Constraints;

/**
 * Created by sasinda on 10/15/15.
 */
public class Login {
    @Constraints.Required
    public String username;
    @Constraints.Required
    public String password;


    public String redirect;
}
