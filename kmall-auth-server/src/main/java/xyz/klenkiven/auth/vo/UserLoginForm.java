package xyz.klenkiven.auth.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * User Login Page Form
 * @author klenkiven
 */
@Data
public class UserLoginForm {

    @NotEmpty(message = "Account MUST not be empty")
    private String account;

    @NotEmpty(message = "Password MUST not be null")
    private String password;

}
