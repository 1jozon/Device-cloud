package cn.rmy.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 定标分析结果
 *
 * @author chu
 * @date 2022/04/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaliAnaDateRes {

    List<CaliAnalysisDataDto> caliAnalysisDataDtos;

    int total;
}
