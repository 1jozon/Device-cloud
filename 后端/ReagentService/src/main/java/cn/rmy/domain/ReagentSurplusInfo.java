package cn.rmy.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
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
@TableName("tb_reagentSurplusInfo")
@Accessors(chain = true)
public class ReagentSurplusInfo implements Serializable {


    //    @IsMobile
    @TableId(type = IdType.AUTO,value = "reagent_surplus_info_id")
    private int reagentSurplusInfoId;

    private String deviceId;

    // 最大30个字符
    private String reagentBoxId;

    // 最大30个字符
    private String reagentNum;

    // 最大30个字符
    private String reagentProj;

    // 试剂余量
    private int reagentSurplus;

//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date getTime;


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
