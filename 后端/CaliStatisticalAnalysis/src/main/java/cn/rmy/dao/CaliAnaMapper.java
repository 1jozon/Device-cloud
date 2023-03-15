package cn.rmy.dao;

import cn.rmy.common.pojo.dto.CaliAnalysisDataDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 定标映射
 *
 * @author chu
 * @date 2022/04/11
 */
@Mapper
public interface CaliAnaMapper {

    /**
     * 全部-不规定时间
     *
     * @return {@link List}<{@link CaliAnalysisDataDto}>
     */
/*    @Select("select b.project_abbr as object, count(a.project_id) as amount " +
            "from cali_data a join tb_project b " +
            "on a.project_id = b.project_id " +
            "group by b.project_abbr " +
            "order by amount DESC " +
            "limit 10 ")*/
    List<CaliAnalysisDataDto> getAllCaliAnalysis(@Param("insList") List<String> authorityInsList);

    /**
     * 全部-规定时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List}<{@link CaliAnalysisDataDto}>
     */
/*    @Select("select b.project_abbr as object, count(a.project_id) as amount " +
            "from cali_data as a, tb_project as b " +
            "where a.cali_time between #{startTime} and #{endTime} " +
            "and a.project_id = b.project_id " +
            "group by b.project_abbr " +
            "order by amount DESC " +
            "limit 10 ")*/
    List<CaliAnalysisDataDto> getAllTimeCaliAnalysis(@Param("insList") List<String> authorityInsList,
                                                     @Param("startTime") Date startTime,
                                                     @Param("endTime") Date endTime);


    /**
     * 项目-规定时间
     * @param projectId
     * @param authorityInsList
     * @param startTime
     * @param endTime
     * @return {@link List}<{@link CaliAnalysisDataDto}>
     */
/*    @Select("select a.ins_id as object, count(a.ins_id) as amount " +
            "from cali_data as a " +
            "where a.project_id = #{projectId} " +
            "and a.cali_time between #{startTime} and #{endTime} " +
            "group by a.ins_id " +
            "order by amount DESC " +
            "limit 10 ")*/
    List<CaliAnalysisDataDto> getProTimeCaliAnalysis(@Param("projectId") String projectId,
                                                     @Param("insList") List<String> authorityInsList,
                                                     @Param("startTime") Date startTime,
                                                     @Param("endTime") Date endTime);

    /**
     * 项目-不规定时间
     * @param projectId
     * @param authorityInsList
     * @return {@link List}<{@link CaliAnalysisDataDto}>
     */
/*    @Select("select a.ins_id as object, count(a.ins_id) as amount " +
            "from cali_data as a " +
            "where a.project_id = #{projectId} " +
            "group by a.ins_id " +
            "order by amount DESC " +
            "limit 10 ")*/
    List<CaliAnalysisDataDto> getProCaliAnalysis(@Param("projectId") String projectId,
                                                 @Param("insList") List<String> authorityInsList);


}
