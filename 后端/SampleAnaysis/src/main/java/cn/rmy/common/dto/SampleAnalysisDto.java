package cn.rmy.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 样品分析结果dto
 *
 * @author chu
 * @date 2022/04/04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleAnalysisDto {

    /**
     * 名称
     */
    private String object;

    /**
     * 数量1
     */
    private Integer number;

    /**
     * 数量2
     */
    private Integer secondNum;

}
