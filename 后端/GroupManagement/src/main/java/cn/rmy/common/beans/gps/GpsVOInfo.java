package cn.rmy.common.beans.gps;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * gps定位信息
 *
 * @author chu
 * @date 2021/11/11
 */
@Data
@TableName("ins_gps")
@AllArgsConstructor
@NoArgsConstructor
public class GpsVOInfo {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 仪器id
     */
    private String instrumentId;

    /**
     * 仪器类型
     */
    private String instrumentMode;

    /**
     * 位置区域码
     */
    private String lac;

    /**
     * 基站编号
     */
    private String cid;

    /**
     * 在线时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date onlineTime;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Version
    private Integer version;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

}
