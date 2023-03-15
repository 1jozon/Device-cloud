package cn.rmy.service;

import cn.rmy.common.dto.SampleAnalysisDto;
import cn.rmy.common.dto.SampleAnalysisResDto;
import cn.rmy.common.dto.SampleConditionDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SampleAnalysisService {

    /**
     * 仪器分类
     *
     * @param condition       条件
     * @param authorityInsIds 权威ins id
     * @return {@link SampleAnalysisResDto}
     */
    SampleAnalysisResDto getSampleInsAnalysis(SampleConditionDto condition, List<String> authorityInsIds);

    /**
     * 项目分类
     *
     * @param condition       条件
     * @param authorityInsIds 权威ins id
     * @return {@link SampleAnalysisResDto}
     */
    SampleAnalysisResDto getSampleProAnalysis(SampleConditionDto condition,List<String> authorityInsIds);

    /**
     * 得到样本年龄分析
     *
     * @param condition       条件
     * @param authorityInsIds 权威ins id
     * @return {@link SampleAnalysisResDto}
     */
    SampleAnalysisResDto getSampleAgeAnalysis(SampleConditionDto condition,List<String> authorityInsIds);

}
