package xyz.klenkiven.kmall.member.vo;

import lombok.Data;

/**
 * Get user info
 * @author klenkiven
 */
@Data
public class UserInfoDTO {

    /** 用户在QQ空间的昵称 */
    private String nickname;

    /** 大小为40×40像素的QQ头像URL */
    private String figureurl_qq_1;

    /** 性别 如果获取不到则默认返回"男" */
    private String gender;

}
