package cn.rmy.common.beans.analysis;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 试剂批次信息
 *
 * @author chu
 * @date 2021/11/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("reagent_batch")
public class ReagentBatchInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String reagentBatchId;

    private Integer caliCount;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date caliTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveTime;

    private Integer caliRotio1;

    private Double caliRotio2;

    private Double caliRotio3;

    private Double caliRotio4;

    private Double caliRotio5;

    private Double caliOffset1;

    private Double caliOffset2;

    private Double caliOffset3;

    private Double caliOffset4;

    private Double caliOffset5;

    private Integer measureType;

    private Double cutoff;

    private Integer sampleType;


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
