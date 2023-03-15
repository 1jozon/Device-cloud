package cn.rmy.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 一句话功能描述.
 * 项目名称:  试剂余量
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:
 * 创建时间:
 */
@Data
@Accessors(chain = true)
public class ReagentSurplusReq implements Serializable {

    //  每天固定时间、换盒，盒用完 按开机自查上次未发送的，均要发送  （？？有疑问 不懂什么意思）
    //  按--天--统计余量

    private String deviceId;

    // 最大30个字符
    private String reagentBoxId;

    // 试剂余量
    private String reagentSurplus;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date getTime;


}
