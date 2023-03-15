package cn.rmy.common.beans.faultManagement;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
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
@TableName("tb_log")
@Accessors(chain = true)
public class Log implements Serializable {

//    @IsMobile
    @TableId(type = IdType.AUTO,value = "log_id")
    private int logId;

    private String operateor;

    private String operateType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date operateDate;

//    private String faultClass;

    private String operateResult;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Version
    private Integer version;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
