package cn.rmy.common.beans.analysis;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 卡利数据信息
 *
 * @author chu
 * @date 2021/12/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("cali_data")
public class CaliDataInfo {
    /**
     * 仪器编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    /**
     * ins id
     */
    private String insId;

    /**
     * 卡利id
     */
    private String caliId;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 试剂批号
     */
    private String reagentBatchId;

    /**
     * 定标点1
     */
    private String caliPointId1;

    /**
     * 定标点2
     */
    private String caliPointId2;

    /**
     * 定标点3
     */
    private String caliPointId3;

    /**
     * 定标点4
     */
    private String caliPointId4;

    /**
     * 定标点5
     */
    private String caliPointId5;

    /**
     * 定标点6
     */
    private String caliPointId6;

    /**
     * 定标点1数值
     */
    private Integer caliPoint1Repeat1;

    /**
     * 定标点1数值
     */
    private Integer caliPoint1Repeat2;

    /**
     * 定标点1数值
     */
    private Integer caliPoint1Repeat3;

    /**
     * 定标点1平均值
     */
    private Integer caliPoint1Average;

    /**
     * 定标点1CV
     */
    private Double caliPoint1Cv;

    /**
     * 定标点1偏差
     */
    private Double caliPoint1Dev;


    /**
     * 定标点2数值
     */
    private Integer caliPoint2Repeat1;

    /**
     * 定标点2数值
     */
    private Integer caliPoint2Repeat2;

    /**
     * 定标点2数值
     */
    private Integer caliPoint2Repeat3;

    /**
     * 定标点2平均值
     */
    private Integer caliPoint2Average;

    /**
     * 定标点2CV
     */
    private Double caliPoint2Cv;

    /**
     * 定标点2偏差
     */
    private Double caliPoint2Dev;

    /**
     * 定标点3数值
     */
    private Integer caliPoint3Repeat1;

    /**
     * 定标点3数值
     */
    private Integer caliPoint3Repeat2;

    /**
     * 定标点3数值
     */
    private Integer caliPoint3Repeat3;

    /**
     * 定标点3平均值
     */
    private Integer caliPoint3Average;

    /**
     * 定标点3CV
     */
    private Double caliPoint3Cv;

    /**
     * 定标点3偏差
     */
    private Double caliPoint3Dev;

    /**
     * 定标点4数值
     */
    private Integer caliPoint4Repeat1;

    /**
     * 定标点4数值
     */
    private Integer caliPoint4Repeat2;

    /**
     * 定标点4数值
     */
    private Integer caliPoint4Repeat3;

    /**
     * 定标点4平均值
     */
    private Integer caliPoint4Average;

    /**
     * 定标点4CV
     */
    private Double caliPoint4Cv;

    /**
     * 定标点4偏差
     */
    private Double caliPoint4Dev;

    /**
     * 定标点5数值
     */
    private Integer caliPoint5Repeat1;

    /**
     * 定标点5数值
     */
    private Integer caliPoint5Repeat2;

    /**
     * 定标点5数值
     */
    private Integer caliPoint5Repeat3;

    /**
     * 定标点5平均值
     */
    private Integer caliPoint5Average;

    /**
     * 定标点5CV
     */
    private Double caliPoint5Cv;

    /**
     * 定标点5偏差
     */
    private Double caliPoint5Dev;

    /**
     * 定标点6数值
     */
    private Integer caliPoint6Repeat1;

    /**
     * 定标点6数值
     */
    private Integer caliPoint6Repeat2;

    /**
     * 定标点6数值
     */
    private Integer caliPoint6Repeat3;

    /**
     * 定标点6平均值
     */
    private Integer caliPoint6Average;

    /**
     * 定标点6CV
     */
    private Double caliPoint6Cv;

    /**
     * 定标点6偏差
     */
    private Double caliPoint6Dev;

    /**
     * 定标时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date caliTime;

    /**
     * 定标有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date effectiveTime;


    /**
     * 定标斜率1
     */
    private Double caliRotio1;

    /**
     * 定标斜率2
     */
    private Double caliRotio2;

    /**
     * 定标斜率3
     */
    private Double caliRotio3;

    /**
     * 定标斜率4
     */
    private Double caliRotio4;

    /**
     * 定标斜率5
     */
    private Double caliRotio5;

    /**
     * 定标偏移量1
     */
    private Double caliOffset1;

    /**
     * 定标偏移量2
     */
    private Double caliOffset2;

    /**
     * 定标偏移量3
     */
    private Double caliOffset3;

    /**
     * 定标偏移量4
     */
    private Double caliOffset4;

    /**
     * 定标偏移量5
     */
    private Double caliOffset5;

    /**
     * 定标浓度1
     */
    private Double cardConc1;

    /**
     * 定标浓度2
     */
    private Double cardConc2;

    /**
     * 定标浓度3
     */
    private Double cardConc3;

    /**
     * 定标浓度4
     */
    private Double cardConc4;

    /**
     * 定标浓度5
     */
    private Double cardConc5;

    /**
     * 定标浓度6
     */
    private Double cardConc6;


    /**
     * 定标RLU1
     */
    private Integer cardRlu1;

    /**
     * 定标RLU2
     */
    private Integer cardRlu2;

    /**
     * 定标RLU3
     */
    private Integer cardRlu3;

    /**
     * 定标RLU4
     */
    private Integer cardRlu4;

    /**
     * 定标RLU5
     */
    private Integer cardRlu5;

    /**
     * 定标RLU6
     */
    private Integer cardRlu6;

    /**
     * 测试类型
     */
    private Integer measureType;

    /**
     * Cutoff值
     */
    private Double cutoff;

    /**
     * 样本类型
     */
    private String sampleType;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Version
    private Integer version;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
