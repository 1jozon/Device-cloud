package cn.rmy.common.beans.faultManagement;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 一句话功能描述.
 * 项目名称:  故障码管理类
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:
 * 创建时间:
 */
@Data
@Accessors(chain = true)
public class FaultHandleReq implements Serializable {

        // 故障处理人员  可为空
    private String userName;

    // 故障处理时间  可为空
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date handleTime;

    private String handleAdvice;

    private String isHandle;

    // 外键——故障记录表ID  根据此来进行故障的处理并记录在处理表中
    private int faultRecordId;

}
