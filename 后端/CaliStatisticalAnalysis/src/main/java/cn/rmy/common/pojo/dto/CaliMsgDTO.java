package cn.rmy.common.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 定标获取数据
 * @author chu
 * @date 2021/11/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaliMsgDTO implements Serializable {

    //定义一个序列号
    private static final long serialVersionUID = 1L;

    /**
     * 仪器编号
     */
    private String insId;


    /**
     * 定标id
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
    private String caliPoint1;

    /**
     * 定标点2
     */
    private String caliPoint2;

    /**
     * 定标点3
     */
    private String caliPoint3;

    /**
     * 定标点4
     */
    private String caliPoint4;

    /**
     * 定标点5
     */
    private String caliPoint5;

    /**
     * 定标点6
     */
    private String caliPoint6;

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
    private Double caliPoint1CV;

    /**
     * 定标点1偏差
     */
    private Double caliPoint1DEV;


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
    private Double caliPoint2CV;

    /**
     * 定标点2偏差
     */
    private Double caliPoint2DEV;

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
    private Double caliPoint3CV;

    /**
     * 定标点3偏差
     */
    private Double caliPoint3DEV;

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
    private Double caliPoint4CV;

    /**
     * 定标点4偏差
     */
    private Double caliPoint4DEV;

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
    private Double caliPoint5CV;

    /**
     * 定标点5偏差
     */
    private Double caliPoint5DEV;

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
    private Double caliPoint6CV;

    /**
     * 定标点6偏差
     */
    private Double caliPoint6DEV;

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
    private Integer cardRLU1;

    /**
     * 定标RLU2
     */
    private Integer cardRLU2;

    /**
     * 定标RLU3
     */
    private Integer cardRLU3;

    /**
     * 定标RLU4
     */
    private Integer cardRLU4;

    /**
     * 定标RLU5
     */
    private Integer cardRLU5;

    /**
     * 定标RLU6
     */
    private Integer cardRLU6;

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


}
