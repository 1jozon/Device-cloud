package cn.rmy.common.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 质控数据dto
 *
 * @author chu
 * @date 2021/12/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualityDataDto {

    private String insId;

    private String projectName;

    private String qctrlRlu;

    private String qctrlResult;

    private String qctrlId;

    private String projectId;

    private String reagentBatchId;

    private int exception;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date qctrlTime;
}
