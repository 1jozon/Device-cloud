package cn.rmy.dao;

import cn.rmy.common.pojo.dto.CaliAnalysisDataDto;
import cn.rmy.common.beans.analysis.CaliDataInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 定标统计
 *
 * @author chu
 * @date 2021/12/27
 */
@Mapper
public interface ChartDao extends BaseMapper<CaliDataInfo> {


    /**
     * 仪器-不规定时间
     *
     * @param insId ins id
     * @return {@link List}<{@link CaliAnalysisDataDto}>
     */
    @Select("select b.project_name as object, count(a.project_id) as amount " +
            "from cali_data as a, tb_project as b " +
            "where a.ins_id = #{insId} " +
            "and a.project_id = b.project_id " +
            "group by b.project_abbr " +
            "order by amount DESC " +
            "limit 10 ")
    List<CaliAnalysisDataDto> getInsCaliAnalysis(@Param("insId") String insId);

    /**
     * 仪器-规定时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param insId     ins id
     * @return {@link List}<{@link CaliAnalysisDataDto}>
     */
    @Select("select b.project_name as object, count(a.project_id) as amount " +
            "from cali_data as a, tb_project as b " +
            "where a.cali_time between #{startTime} and #{endTime} " +
            "and a.ins_id = #{insId} " +
            "and a.project_id = b.project_id " +
            "group by b.project_abbr " +
            "order by amount DESC " +
            "limit 10 ")
    List<CaliAnalysisDataDto> getInsTimeCaliAnalysis(@Param("insId") String insId,
                                                     @Param("startTime") Date startTime,
                                                     @Param("endTime") Date endTime);

    /**
     * 得到仪器、项目、时间分析
     *
     * @param insId     ins id
     * @param projectId 项目id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List}<{@link CaliAnalysisDataDto}>
     */
    @Select("select b.project_name as object, count(a.project_id) as amount " +
            "from cali_data as a, tb_project as b " +
            "where a.cali_time between #{startTime} and #{endTime} " +
            "and a.ins_id = #{insId} " +
            "and a.project_id = #{projectId}" +
            "and a.project_id = b.project_id " +
            "group by b.project_abbr " +
            "order by amount DESC " +
            "limit 10 ")
    List<CaliAnalysisDataDto> getInsProTimeCaliAnalysis(@Param("insId") String insId,
                                                        @Param("projectId") String projectId,
                                                        @Param("startTime") Date startTime,
                                                        @Param("endTime") Date endTime);

    /**
     * 得到ins pro卡利分析
     * 得到仪器、项目、时间分析
     *
     * @param insId     ins id
     * @param projectId 项目id
     * @return {@link List}<{@link CaliAnalysisDataDto}>
     */
    @Select("select b.project_name as object, count(a.project_id) as amount " +
            "from cali_data as a, tb_project as b " +
            "where a.ins_id = #{insId} " +
            "and a.project_id = #{projectId}" +
            "and a.project_id = b.project_id " +
            "group by b.project_abbr " +
            "order by amount DESC " +
            "limit 10 ")
    List<CaliAnalysisDataDto> getInsProCaliAnalysis(@Param("insId") String insId,
                                                    @Param("projectId") String projectId);


}
