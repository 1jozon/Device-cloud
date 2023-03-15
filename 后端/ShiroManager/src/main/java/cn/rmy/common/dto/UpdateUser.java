package cn.rmy.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUser {
    private String userId;

    private String userName;

    private String userPhone;

    private int userGender;

    private String userAvatar;

    /**
     * 用户的电子邮件
     */
    private String userEmail;

    /**
     * 验证码
     */
    private String verificationCode;

    private String newPassword;

    private int[] roleIds;
}
