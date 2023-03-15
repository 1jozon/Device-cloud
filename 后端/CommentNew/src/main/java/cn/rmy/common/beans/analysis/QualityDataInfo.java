package cn.rmy.common.beans.analysis;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 质控数据信息
 *
 * @author chu
 * @date 2021/12/21
 */
@Data
@TableName("quality_analysis")
@AllArgsConstructor
@NoArgsConstructor
public class QualityDataInfo {

    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    private String insId;

    private String projectName;

    private String qctrlRlu;

    private String qctrlResult;

    private String qctrlId;

    private String projectId;

    private String reagentBatchId;

    private int exception;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date qctrlTime;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @Version
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer version;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
