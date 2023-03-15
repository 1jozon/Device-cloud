package cn.rmy.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 质控指定项目 返回结果
 *
 * @author chu
 * @date 2022/04/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualityAnaProRes {

    private List<QualityAnalysisProjectDto> qualityAnalysisProjectDtos;

    private int total;
}
