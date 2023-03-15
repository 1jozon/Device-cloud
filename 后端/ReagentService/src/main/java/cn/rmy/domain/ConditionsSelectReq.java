package cn.rmy.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 一句话功能描述.
 * 项目名称:  条件查询请求类

 */
@Data
@Accessors(chain = true)
public class ConditionsSelectReq {
    // 1:按试剂分类，2：按仪器分类
    private int countByReagentOrDevice;

    private String userId;

    // 最大30个字符  项目号
    private String reagentNum;

    private String deviceId;
    // 条件为：0：自定义（日期范围查询），1：最近一天，2：最近一周，3：最近一个月，4：最近三个月
    private int nearlyDay;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")  //  当为自定义的时候该字段有值，否则即便有值，我也不会去取
    private Date beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")  //  当为自定义的时候该字段有值，否则即便有值，我也不会去取
    private Date endTime;
}
