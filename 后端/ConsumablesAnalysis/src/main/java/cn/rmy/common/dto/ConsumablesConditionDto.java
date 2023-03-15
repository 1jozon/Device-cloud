package cn.rmy.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 筛选条件
 *
 * @author chu
 * @date 2021/12/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumablesConditionDto {

    int all;

    private String userId;

    private String insId;

    private String consumName;

    private int solidState;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

}
