package cn.rmy.common.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 质量筛选条件
 *
 * @author chu
 * @date 2021/12/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualityConditionDto {

    private String userId;

    private int all;

    private String insId;

    private String projectName;

    private Integer exception;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
