package cn.rmy.service;

import cn.rmy.common.dto.ConAnalysisDto;
import cn.rmy.common.dto.ConAnalysisResDto;
import cn.rmy.common.dto.ConsumablesConditionDto;

import java.util.List;

public interface ConAnalysisService {

    /**
     * 得到仪器耗材分析
     *
     * @param condition         条件
     * @param authorityInsList authorithy ins列表
     * @return {@link List}<{@link ConAnalysisDto}>
     */
    ConAnalysisResDto getInsAnalysis(ConsumablesConditionDto condition, List<String> authorityInsList);


    /**
     * 得到耗材名称分析
     *
     * @param condition         条件
     * @param authorityInsList authorithy ins列表
     * @return {@link List}<{@link ConAnalysisDto}>
     */
    ConAnalysisResDto getConAnalysis(ConsumablesConditionDto condition,List<String> authorityInsList);
}
