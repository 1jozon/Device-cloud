package cn.rmy.service;

import cn.rmy.common.pojo.dto.*;

import java.util.List;

public interface AnalysisService {

    /**
     * 仪器质控分析
     *
     * @param condition    条件
     * @param authorithIns
     * @return {@link List}<{@link QualityAnalysisInsDto}>
     */
    QualityAnaInsRes qualityInsAnalysis(QualityConditionDto condition, List<String> authorithIns);


    /**
     * 项目质控分析
     *
     * @param condition    条件
     * @param authorithIns
     * @return {@link List}<{@link QualityAnalysisProjectDto}>
     */
    QualityAnaProRes qualityProAnalysis(QualityConditionDto condition, List<String> authorithIns);

    /**
     * 把所有项目名称
     * 所有项目名称
     *
     * @param projectName 项目名称
     * @return {@link List}<{@link ProjectDataDto}>
     */
    List<ProjectDataDto> getAllProjectNames(String projectName);
}
