package cn.rmy.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 质控指定ins 返回结果
 *
 * @author chu
 * @date 2022/04/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QualityAnaInsRes {

    private List<QualityAnalysisInsDto> qualityAnalysisInsDtos;

    private int total;
}
