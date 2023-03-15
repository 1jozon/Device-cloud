package cn.rmy.common.pojo.dto.emaildto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮件
 *
 * @author chu
 * @date 2021/11/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {

    /**
     * 主题
     */
    private String subject;

    /**
     * 内容
     */
    private String content;

    /**
     * 收件人列表
     */
    private String[] tos;
}
