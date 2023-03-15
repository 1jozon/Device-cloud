package cn.rmy.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 耗材统计结果 dto
 *
 * @author chu
 * @date 2022/04/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConAnalysisResDto {

    List<ConAnalysisDto> conAnalysisList;

    int total;
}
