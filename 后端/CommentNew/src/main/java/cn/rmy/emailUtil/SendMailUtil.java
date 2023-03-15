package cn.rmy.emailUtil;

import cn.rmy.common.pojo.dto.emaildto.MailDto;
import cn.rmy.common.redisUtils.CommonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 阿里云邮件推送
 *
 * @author chu
 * @date 2021/11/30
 */
public class SendMailUtil {

    private static Log logger = LogFactory.getLog(SendMailUtil.class);

    private static final String ALIDM_SMTP_HOST = "smtpdm.aliyun.com";
    private static final String ALIDM_SMTP_PORT = "80";
    private static final String AlIDM_SMTP_USER = "rmy@ycdsjtec.cn";
    private static final String ALIDM_SMTP_PASSWORD = "RMYrmy1234";
    /**
     * 发送
     */
    public static void send(MailDto mailDto) throws org.springframework.messaging.MessagingException {
        //配置发送邮件环境属性
        final Properties props = new Properties();
        //身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", ALIDM_SMTP_HOST);

        //使用SSL加密SMTP通过465端口进行邮件发送
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", "80");
        props.put("mail.smtp.port", ALIDM_SMTP_PORT);

        //发件人
        props.put("mail.user", AlIDM_SMTP_USER);
        props.put("mail.password", ALIDM_SMTP_PASSWORD);

        //构建授权信息，身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        //使用环境属性和授权信息创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        //创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        try {
            //设置发件人邮件地址和名称
            InternetAddress from = new InternetAddress(AlIDM_SMTP_USER, "rmytest");
            message.setFrom(from);
            //收件人个数
            int size = mailDto.getTos().length;
            //设置收件人邮件地址
            InternetAddress[] addresses = new InternetAddress[size];
            for (int i = 0, j = 0; i < size; i++) {
                String email = mailDto.getTos()[i];
                if (email == null || email.length() == 0) {
                    continue;
                } else {
                    addresses[j++] = new InternetAddress(email);
                }
            }
            if (addresses[0] == null) {
                return;
            }
            List<InternetAddress> addressList = new ArrayList<>();
            for (InternetAddress address : addresses) {
                if(CommonUtil.isNotNull(address))
                    addressList.add(address);
            }
            InternetAddress[] addresses1 = new InternetAddress[addressList.size()];
            for(int i=0;i<addresses1.length;i++){
                addresses1[i] = addressList.get(i);
            }
            message.setRecipients(Message.RecipientType.TO, addresses1);
            //设置邮件标题
            message.setSubject(mailDto.getSubject());
            //设置邮件内容
            message.setContent(mailDto.getContent(), "text/html;charset=UTF-8");
            //添加附件（这没写）
            //发送邮件
            Transport.send(message);


        } catch (MessagingException | UnsupportedEncodingException e) {
            String err = e.getMessage();
            logger.info(err);
        }
    }
}
