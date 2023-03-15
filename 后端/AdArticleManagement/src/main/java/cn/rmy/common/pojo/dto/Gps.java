package cn.rmy.common.pojo.dto;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 全球定位系统(gps)
 *
 * @author chu
 * @date 2021/11/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gps {

    /**
     * 仪器id
     */
    private String InstrumentId;

    /**
     * 仪器类型
     */
    private String InstrumentMode;

    /**
     * 位置区域码
     */
    private String lac;

    /**
     * 基站编号
     */
    public String cid;

    /**
     * 接受次数
     */
    private int count;

    /**
     * 在线时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date onlineTime;



}
