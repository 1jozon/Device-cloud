package cn.rmy.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleConditionDto {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 仪器id
     */
    private String insId;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目分类 0-仪器分析，1-项目分析
     */
    private int proType;

    /**
     * 异常 1-年龄异常，0-年龄正常
     */
    private int exception;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
