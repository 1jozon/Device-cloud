package cn.rmy.common.beans.analysis;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 试剂卡利点信息
 * 试剂批号-定标点关系库
 *
 * @author chu
 * @date 2021/11/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("reagentbatch_to_calipoint")
public class ReagentToCaliPointInfo {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer reagentBatchIdId;

    private Integer caliPointIdId;

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
