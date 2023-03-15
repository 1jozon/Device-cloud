package cn.rmy.service;

import cn.rmy.common.beans.analysis.QualityDataInfo;
import cn.rmy.common.pojo.dto.QualityConditionDto;
import cn.rmy.common.pojo.dto.QualityDataDto;
import cn.rmy.common.pojo.dto.SelectResult;

public interface QualityService {

    /**
     * 获得质控信息
     *
     * @param qualityData 质量数据
     * @return {@link QualityDataInfo}
     */
    QualityDataInfo getQualityInfo(QualityDataDto qualityData);

    /**
     * 插入质控信息
     *
     * @param qualityData 质量数据
     * @return int
     */
    int insertQualityInfo(QualityDataDto qualityData);

    /**
     * 异步插入质量信息
     *
     * @param qualityData 质量数据
     */
    void AsyncInsertQualityInfo(QualityDataDto qualityData);

    /**
     * 更新质控信息
     *
     * @param qualityDataInfo 高质量的数据信息
     * @return int
     */
    int updateQualityInfo(QualityDataInfo qualityDataInfo);

    /**
     * 通过条件分页查询
     *
     * @param current   当前的
     * @param size      大小
     * @param condition 条件
     * @return {@link SelectResult}
     */
    SelectResult getByCondition(int current, int size, QualityConditionDto condition);

    /**
     * 通过id获取详细质控数据
     *
     * @param id id
     * @return {@link QualityDataInfo}
     */
    QualityDataInfo getById(int id);

}
