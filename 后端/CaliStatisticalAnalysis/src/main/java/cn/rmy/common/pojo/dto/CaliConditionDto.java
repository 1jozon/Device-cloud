package cn.rmy.common.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 定标筛选条件
 *
 * @author chu
 * @date 2021/12/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaliConditionDto {

    private int all;

    private String userId;

    private String insId;

    private String projectId;

    private String reagentBatchId;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
