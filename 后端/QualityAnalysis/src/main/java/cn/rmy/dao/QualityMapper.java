package cn.rmy.dao;

import cn.rmy.common.pojo.dto.QualityAnalysisInsDto;
import cn.rmy.common.pojo.dto.QualityAnalysisProjectDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface QualityMapper {

    /**
     * 得到所有仪器分析 默认-全部时间段
     *
     * @param authorityIns 权威的国际新闻社
     * @return {@link List}<{@link QualityAnalysisInsDto}>
     */
    List<QualityAnalysisInsDto> getAllInsAnalysis(@Param("insList") List<String> authorityIns);

    /**
     * 得到所有仪器分析 指定时间条件
     *
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @param authorityIns 权威的国际新闻社
     * @return {@link List}<{@link QualityAnalysisInsDto}>
     */
    List<QualityAnalysisInsDto> getAllTimeInsAnalysis(@Param("insList") List<String> authorityIns,
                                                      @Param("startTime") Date startTime,
                                                      @Param("endTime") Date endTime);

    /**
     * 得到指定ins分析 全部时间段
     *
     * @param insId ins id
     * @return {@link List}<{@link QualityAnalysisInsDto}>
     */
/*    @Select("select a.ins_id, a.project_name, count(*) AS pro_count, sum(a.exception) AS exception_count " +
            "from quality_analysis AS a " +
            "where a.ins_id = #{insId}" +
            "group by a.project_name " +
            "order by pro_count DESC " +
            "limit 10")*/
    List<QualityAnalysisInsDto> getInsAnalysis(@Param("sinInsId") String insId);

    /**
     * 得到指定ins分析  指定时间段
     *
     * @param insId     ins id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List}<{@link QualityAnalysisInsDto}>
     */
/*    @Select("select a.ins_id, a.project_name, count(*) AS pro_count, sum(a.exception) AS exception_count " +
            "from quality_analysis AS a " +
            "where a.qctrl_time between #{startTime} and #{endTime} " +
            "and a.ins_id = #{insId} " +
            "group by a.project_name " +
            "order by pro_count DESC " +
            "limit 10")*/
    List<QualityAnalysisInsDto> getTimeInsAnalysis(@Param("sinInsId") String insId,
                                                   @Param("startTime") Date startTime,
                                                   @Param("endTime") Date endTime);

    /*项目*/

    /**
     * 得到指定项目-指定仪器-指定时间 分析
     *
     * @param projectName 项目名称
     * @param insId       ins id
     * @param starTime    明星的时间
     * @param endTime     结束时间
     * @return {@link List}<{@link QualityAnalysisProjectDto}>
     */
    List<QualityAnalysisProjectDto> getProInsTimeAnalysis(@Param("projectName") String projectName,
                                                          @Param("sinInsId") String insId,
                                                          @Param("startTime") Date starTime,
                                                          @Param("endTime") Date endTime);
    /**
     * 得到 指定项目-指定仪器-所有时间 分析
     *
     * @param projectName 项目名称
     * @param insId       ins id
     * @return {@link List}<{@link QualityAnalysisProjectDto}>
     */
    List<QualityAnalysisProjectDto> getProInsAnalysis(@Param("projectName") String projectName,
                                                          @Param("sinInsId") String insId);
    /**
     * 得到 指定项目-所有仪器-所有时间 分析
     *
     * @param projectName 项目名称
     * @return {@link List}<{@link QualityAnalysisProjectDto}>
     */
/*    @Select("select quality_analysis.project_name As project_name, " +
            "tb_instrument.instrument_model As ins_model, " +
            "count(tb_instrument.instrument_model) AS ins_model_count, " +
            "sum(quality_analysis.exception) AS exception_count " +
            "from quality_analysis inner join tb_instrument " +
            "on quality_analysis.ins_id = tb_instrument.instrument_id " +
            "and quality_analysis.project_name = #{projectName} " +
            "group by tb_instrument.instrument_model " +
            "order by ins_model_count DESC ")*/
    List<QualityAnalysisProjectDto> getProAnalysis(@Param("projectName") String projectName,
                                                   @Param("insList") List<String> authorityIns);

    /**
     * 得到 指定项目-指定仪器-指定时间 分析
     *
     * @param projectName  项目名称
     * @param authorityIns 权威的国际新闻社
     * @param starTime     明星的时间
     * @param endTime      结束时间
     * @return {@link List}<{@link QualityAnalysisProjectDto}>
     *//*    @Select("select quality_analysis.project_name As project_name, tb_instrument.instrument_model As ins_model, " +
            "count(tb_instrument.instrument_model) AS ins_model_count, " +
            "sum(quality_analysis.exception) AS exception_count " +
            "from quality_analysis inner join tb_instrument " +
            "on quality_analysis.ins_id = tb_instrument.instrument_id " +
            "and quality_analysis.project_name = #{projectName} " +
            "and quality_analysis.qctrl_time between #{startTime} and #{endTime} " +
            "group by tb_instrument.instrument_model " +
            "order by ins_model_count DESC ")*/
    List<QualityAnalysisProjectDto> getProTimeAnalysis(@Param("projectName") String projectName,
                                                       @Param("insList") List<String> authorityIns,
                                                       @Param("startTime") Date starTime,
                                                       @Param("endTime") Date endTime);

}
