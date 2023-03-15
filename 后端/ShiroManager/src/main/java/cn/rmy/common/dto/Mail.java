package cn.rmy.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mail {

    //发件人地址
    private String sendUser;

    //主题
    private String subject;

    //主题内容
    private String content;

    //接收人列表
    private String[] toMail;

}
