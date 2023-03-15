package cn.rmy.dao;

import cn.rmy.common.dto.SampleAnalysisDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 样本映射器
 *
 * @author chu
 * @date 2022/04/11
 */
@Mapper
public interface SampleMapper {

    /**
     * 所有仪器-仪器分类-无时间
     *
     * @param authorityIns 权威的国际新闻社
     * @return {@link List}<{@link SampleAnalysisDto}>
     */
    List<SampleAnalysisDto> getSampleAnaInsAll(@Param("insList") List<String> authorityIns);

    /**
     * 所有仪器-仪器分类-有时间
     *
     * @param authorityIns 权威的国际新闻社
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return {@link List}<{@link SampleAnalysisDto}>
     */
    List<SampleAnalysisDto> getSampleAnaInsTime(@Param("insList") List<String> authorityIns,
                                                @Param("startTime")Date startTime,
                                                @Param("endTime") Date endTime);

    /**
     * 所有仪器-所有时间-项目
     *
     * @param authorityIns 权威的国际新闻社
     * @return {@link List}<{@link SampleAnalysisDto}>
     */
    List<SampleAnalysisDto> getSampleAnaProAllIns(@Param("insList") List<String> authorityIns);

    /**
     * 所有仪器-指定时间-项目
     *
     * @param authorityIns 权威的国际新闻社
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return {@link List}<{@link SampleAnalysisDto}>
     */
    List<SampleAnalysisDto> getSampleAnaProAllInsTime(@Param("insList") List<String> authorityIns,
                                                      @Param("startTime")Date startTime,
                                                      @Param("endTime") Date endTime);


    /**
     * 指定仪器-所有时间-项目
     *
     * @param insId ins id
     * @return {@link List}<{@link SampleAnalysisDto}>
     */
    List<SampleAnalysisDto> getSampleAnaProSinIns(@Param("insId") String insId);

    /**
     * 指定仪器-指定时间-项目
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param insId     ins id
     * @return {@link List}<{@link SampleAnalysisDto}>
     */
    List<SampleAnalysisDto> getSampleAnaProSinInsTime(@Param("insId") String insId,
                                                      @Param("startTime")Date startTime,
                                                      @Param("endTime") Date endTime);


    /**
     * 得到年龄分析
     *
     * @param projectId 项目id
     * @param insId     ins id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List}<{@link SampleAnalysisDto}>
     */
    List<SampleAnalysisDto> getSampleAnaAgeSinIns(@Param("projectId") String projectId,
                                            @Param("insId") String insId,
                                            @Param("startTime")Date startTime,
                                            @Param("endTime") Date endTime);

    /**
     * 得到年龄分析 - 有异常
     *
     * @param projectId 项目id
     * @param insId     ins id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List}<{@link SampleAnalysisDto}>
     */
    List<SampleAnalysisDto> getSampleAnaAgeSinInsExp(@Param("projectId") String projectId,
                                                     @Param("insId") String insId,
                                                     @Param("startTime")Date startTime,
                                                     @Param("endTime") Date endTime);


    /**
     * 得到年龄分析 - 无异常
     *
     * @param projectId    项目id
     * @param authorityIns 权威的国际新闻社
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return {@link List}<{@link SampleAnalysisDto}>
     */
    List<SampleAnalysisDto> getSampleAnaAgeAllIns(@Param("projectId") String projectId,
                                            @Param("insList") List<String> authorityIns,
                                            @Param("startTime")Date startTime,
                                            @Param("endTime") Date endTime);

    /**
     * 得到年龄分析 - 有异常
     *
     * @param projectId    项目id
     * @param authorityIns 权威的国际新闻社
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return {@link List}<{@link SampleAnalysisDto}>
     */
    List<SampleAnalysisDto> getSampleAnaAgeAllInsExp(@Param("projectId") String projectId,
                                                     @Param("insList") List<String> authorityIns,
                                                     @Param("startTime")Date startTime,
                                                     @Param("endTime") Date endTime);

}
