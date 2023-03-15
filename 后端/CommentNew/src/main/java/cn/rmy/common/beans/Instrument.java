package cn.rmy.common.beans;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tb_instrument")
public class Instrument implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    private String instrumentId;

    private String instrumentModel;

    private String instrumentAddress;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date instrumentDate;

    //在线状态，默认断开，取值{-1：断开，1：在线}
    private int onlineStatus;

    //在线状态，默认正常，取值{-1：故障，1：正常}
    private int faultStatus;

    private String instrumentInstallerId;

    private String instrumentInstallerName;

    private String instrumentMaintainerId;

    private String instrumentMaintainerName;

    private String instrumentMaintainerPhone;

    private String hospitalName;

    @TableField(fill = FieldFill.INSERT)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Version
    private Integer version;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

}
