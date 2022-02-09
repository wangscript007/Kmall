package xyz.klenkiven.auth.model;

import lombok.Data;

/**
 * QQ Access Token
 * @author klenkiven
 */
@Data
public class QQAccessToken {

    /** 授权令牌，Access_Token */
    private String access_token;

    /** 该access token的有效期，单位为秒 */
    private String expires_in;

    /** 在授权自动续期步骤中，获取新的Access_Token时需要提供的参数 */
    private String refresh_token;

}
