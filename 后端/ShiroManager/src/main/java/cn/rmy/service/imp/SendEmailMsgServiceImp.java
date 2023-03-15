package cn.rmy.service.imp;

import cn.rmy.common.beans.shiroUsers.UserInfo;
import cn.rmy.common.pojo.dto.emaildto.MailDto;
import cn.rmy.emailUtil.SendMailUtil;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SendEmailMsgServiceImp implements cn.rmy.service.SendEmailMsgService {


    /**
     * 发送注册等待审核邮件通知
     *
     * @param userInfo 用户信息
     */
    @Override
    public void sendRegistEmailMsg(UserInfo userInfo) {
        MailDto mail = new MailDto();
        mail.setSubject("【仁迈云】注册通知");
        String content = "尊敬的"+userInfo.getUserId() +"用户，您好：\n" + "您注册仁迈云用户成功，正在等待管理员审核，" +
                "审核结果稍后会邮件通知您，请您耐心等待。";
        mail.setContent(content);
        String[] emails = new String[1];
        emails[0] = userInfo.getUserEmail();
        mail.setTos(emails);

        SendMailUtil.send(mail);
    }

    /**
     * 发送批准通过通知
     *
     * @param userInfo 用户信息
     */
    @Override
    public void sendApproveEmailMsg(UserInfo userInfo) {

        int status = userInfo.getRegistStatus();

        MailDto mail = new MailDto();
        mail.setSubject("【仁迈云】注册审批通知");
        String content = "";
        if (status == 2){
            content = "尊敬的"+userInfo.getUserId() +"用户，您好：\n" + "您的账户审核已通过，可以正常使用！";
        }else if(status == 3) {
            content = "尊敬的"+userInfo.getUserId() +"用户，您好：\n" + "您注册仁迈云用户失败，管理员拒绝了您的新用户申请，请联系管理员进行规范注册！";
        }else if(status == 4){
            content = "尊敬的"+userInfo.getUserId() +"用户，您好：\n" + "您的账户已被管理员禁用，请联系管理员启用后使用！";
        }

        mail.setContent(content);
        String[] emails = new String[1];
        emails[0] = userInfo.getUserEmail();
        mail.setTos(emails);

        SendMailUtil.send(mail);
    }

    /**
     * 发送验证邮件
     *
     * @param code     代码
     * @param userInfo 用户信息
     */
    @Override
    public void sendVerifyEmailMsg(String code, UserInfo userInfo) {
        MailDto mail = new MailDto();
        mail.setSubject("【仁迈云】验证通知");

        String content = "您正在修改邮箱，验证码" + code + "切勿将验证码泄露与他人，本次验证码有效期为15分钟。";

        mail.setContent(content);
        String[] emails = new String[1];
        emails[0] = userInfo.getUserEmail();
        mail.setTos(emails);

        SendMailUtil.send(mail);

    }

    /**
     * 发送电子邮件通知
     *
     * @param type   类型:1-审核等待通知、2-审核通过通知、3-审核拒绝通知、4-验证码通知
     * @param userId 用户id
     */
    @Override
    public void sendEmailMsg(int type, String userId) {

    }

    /**
     * 验证码
     *
     * @return {@link String}
     */
    @Override
    public String verificationCode() {
        String[] beforeShuffle= new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
                "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z" };
        StringBuffer s = new StringBuffer();
        Random rand = new Random();
        for (int i = 0; i < 4; i++){
            //随机4位数
            s.append(beforeShuffle[rand.nextInt(beforeShuffle.length)]);
        }
        return s.toString();
    }
}
