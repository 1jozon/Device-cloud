package cn.rmy.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 样品分析结果 dto
 *
 * @author chu
 * @date 2022/04/06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleAnalysisResDto {

    private List<SampleAnalysisDto> sampleAnalysisList;

    private Integer total;

}
