package cn.rmy.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 项目名称:  用于故障统计   满足需求1
 */
@Data
@Accessors(chain = true)
public class FaultRecordCountDto {

    private String deviceId;

    private int faultCountNum;

}
