package cn.rmy.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 质控项目分析
 *
 * @author chu
 * @date 2021/12/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualityAnalysisProjectDto {

    private String projectName;

    private String insModel;

    int insModelCount;

    int exceptionCount;

}
