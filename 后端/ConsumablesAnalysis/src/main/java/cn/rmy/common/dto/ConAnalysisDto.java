package cn.rmy.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分析返回数据
 *
 * @author chu
 * @date 2021/12/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConAnalysisDto {

    private String objectName;

    private int amount;
}
