package xyz.klenkiven.auth.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * Register Form
 * @author klenkiven
 */
@Data
public class RegForm {
    @NotEmpty(message = "Username cannot be empty")
    @Length(min = 6, max = 18, message = "Username length must between 6 and 18 characters")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @Length(min = 6, max = 18, message = "Password length must between 6 and 18 characters")
    private String password;

    @NotEmpty(message = "Phone cannot be empty")
    @Pattern(regexp = "^[1]([3-9])[0-9]{9}$", message = "Phone's format is error")
    private String phone;

    @NotEmpty(message = "Code cannot be empty")
    private String code;

}
