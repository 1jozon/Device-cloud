package cn.rmy.service;

import cn.rmy.common.pojo.dto.CaliAnaDateRes;
import cn.rmy.common.pojo.dto.CaliConditionDto;

import java.util.List;

/**
 * 定标数据图表服务
 *
 * @author chu
 * @date 2021/11/19
 */
public interface ChartService {


    /**
     * 定标分析
     *
     * @param condition 条件
     * @param authorityInsList
     * @return {@link CaliAnaDateRes}
     */
    CaliAnaDateRes getAnalysis(CaliConditionDto condition, List<String> authorityInsList);
}
