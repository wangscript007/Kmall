package xyz.klenkiven.kmall.cart.vo;

import lombok.Data;

/**
 * User Info - Record UserID, temp User-ID and isTempUser
 * @author klenkiven
 */
@Data
public class UserInfoVO {

    /** Login User's ID */
    private Long UserId;

    /** Temp User Key */
    private String userKey;

    /** Has User Key in User Agent */
    private Boolean tempUser = false;

}
