package cn.rmy.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 质控仪器分析
 *
 * @author chu
 * @date 2021/12/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualityAnalysisInsDto {

    String insId;

    String projectName;

    int proCount;

    int exceptionCount;
}
