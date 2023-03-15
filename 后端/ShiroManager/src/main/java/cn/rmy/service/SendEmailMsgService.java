package cn.rmy.service;

import cn.rmy.common.beans.shiroUsers.UserInfo;

public interface SendEmailMsgService {

    /**
     * 发送注册等待审核邮件通知
     *
     * @param userInfo 用户信息
     */
    void sendRegistEmailMsg(UserInfo userInfo);

    /**
     * 发送批准通过通知
     *
     * @param userInfo 用户信息
     */
    void sendApproveEmailMsg(UserInfo userInfo);

    /**
     * 发送验证邮件
     *
     * @param code     代码
     * @param userInfo 用户信息
     */
    void sendVerifyEmailMsg(String code, UserInfo userInfo);

    /**
     * 发送电子邮件味精
     * 发送电子邮件通知
     *
     * @param type   类型:1-审核等待通知、2-审核通过通知、3-验证码通知
     * @param userId 用户id
     */
    void sendEmailMsg(int type, String userId);

    /**
     * 验证码
     *
     * @return {@link String}
     */
    String verificationCode();

}
