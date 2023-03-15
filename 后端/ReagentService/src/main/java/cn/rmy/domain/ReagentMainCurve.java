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
 * 项目名称:  试剂卡信息
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:
 * 创建时间:
 */
@Data
@TableName("tb_reagentMainCurve")
@Accessors(chain = true)
public class ReagentMainCurve implements Serializable {


    @TableId(type = IdType.AUTO,value = "reagent_main_curve_id")
    private int reagentMainCurveId;

    private String deviceId;

    // 最大30个字符
    private String reagentBatchId;

    private int rlu1;
    private int rlu2;
    private int rlu3;
    private int rlu4;
    private int rlu5;
    private int rlu6;
    private int rlu7;
    private int rlu8;
    private int rlu9;
    private int rlu10;

    private double conc1;
    private double conc2;
    private double conc3;
    private double conc4;
    private double conc5;
    private double conc6;
    private double conc7;
    private double conc8;
    private double conc9;
    private double conc10;

    // 主曲线类型
    private int curveType;

    private double paramA;
    private double paramB;
    private double paramC;
    private double paramD;
    private double paramE;

    // 总测试数
    private int totalNum;

    //定标点数
    private int caliNum;

    //有效期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")  //  当为自定义的时候该字段有值，否则即便有值，我也不会去取
    private Date effectiveTime;

    private double nsb0;

    private int rluMax0;

    private double proCon;

    private double proRlu;

    private double diluRatio;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")  //  当为自定义的时候该字段有值，否则即便有值，我也不会去取
    private Date getTime;

    // 样本类型
    private String sampleType;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")  //  当为自定义的时候该字段有值，否则即便有值，我也不会去取
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")  //  当为自定义的时候该字段有值，否则即便有值，我也不会去取
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Version
    private Integer version;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
