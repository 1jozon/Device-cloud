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
 * 项目名称:   故障处理信息
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:   xsc
 * 创建时间:
 */
@Data
@TableName("tb_fault_record")
@Accessors(chain = true)
public class FaultRecord implements Serializable {

    //    @IsMobile
    @TableId(type = IdType.AUTO,value = "fault_record_id")
    private int faultRecordId;

    private String deviceId;

    private String deviceType;

    private String faultCode;

    private String faultDescribe;

    private String faultAdvice;

//    private String faultType;

    private String moduleCode;

    private String faultClass;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date faultTime;


    // 处理状态  -1 未处理 1 已处理
    private String handleStatus;



    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Version
    private Integer version;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
