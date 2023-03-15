package cn.rmy.common.beans;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("legal_instrument")
public class LegalInstrument {
    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    private String instrumentId;

    private String instrumentModel;

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
