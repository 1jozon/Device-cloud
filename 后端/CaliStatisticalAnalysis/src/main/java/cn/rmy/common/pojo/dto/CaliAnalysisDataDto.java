package cn.rmy.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 定标分析data
 *
 * @author chu
 * @date 2021/12/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaliAnalysisDataDto {

    private String object;

    private int amount;

}
