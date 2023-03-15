package cn.rmy.common.beans.analysis;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 仪器-试剂批号
 * @author chu
 * @date 2021/11/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("ins_to_reagentbatch")
public class InsToReagentBatchInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String instrumentId;

    private Integer reagentBatchIdId;

    private String reagentBatchId;


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
