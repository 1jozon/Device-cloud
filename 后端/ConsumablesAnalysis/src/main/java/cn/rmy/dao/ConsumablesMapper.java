package cn.rmy.dao;

import cn.rmy.common.dto.ConAnalysisDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface ConsumablesMapper {

    /**
     * 分析 全部-不规定时间
     *
     * @return {@link List}<{@link ConAnalysisDto}>
     */
/*    @Select("select a.con_name AS objectName, sum(a.vol_used) as amount " +
            "from consumables_data AS a " +
            "where a.solid_state = #{solidState} " +
            "group by a.con_name " +
            "order by amount DESC " +
            "limit 5")*/
    List<ConAnalysisDto> conGetAllAnalysis(@Param("solidState") int solidState,
                                           @Param("insList") List<String> authorityInsList);

    /**
     * 分析 全部-规定时间
     *
     *
     * @param authorityInsList
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List}<{@link ConAnalysisDto}>
     */
/*    @Select("select a.con_name AS objectName, sum(a.vol_used) as amount " +
            "from consumables_data AS a " +
            "where a.solid_state = #{solidState} " +
            "and a.date_time between #{startTime} and #{endTime} " +
            "group by a.con_name " +
            "order by amount DESC " +
            "limit 5")*/
    List<ConAnalysisDto> conGetAllTimeAnalysis(@Param("solidState") int solidState,
                                               @Param("insList") List<String> authorityInsList,
                                               @Param("startTime")Date startTime,
                                               @Param("endTime") Date endTime);


    /*指定耗材*/

    /**
     * 分析 耗材-不规定时间
     *
     * @param conName 反对的名字
     * @return {@link List}<{@link ConAnalysisDto}>
     */
/*    @Select("select a.ins_id AS objectName, sum(a.vol_used) as amount " +
            "from consumables_data AS a " +
            "where a.con_name = #{conName} " +
            "group by a.ins_id " +
            "order by amount DESC " +
            "limit 10")*/
    List<ConAnalysisDto> conGetConAnalysis(@Param("conName") String conName,
                                           @Param("insList") List<String> authorityInsList);

    /**
     * 分析 耗材-规定时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param conName   反对的名字
     * @return {@link List}<{@link ConAnalysisDto}>
     */
/*    @Select("select a.ins_id AS objectName, sum(a.vol_used) as amount " +
            "from consumables_data AS a " +
            "where a.con_name = #{conName} " +
            "and a.date_time between #{startTime} and #{endTime} " +
            "group by a.ins_id " +
            "order by amount DESC " +
            "limit 10")*/
    List<ConAnalysisDto> conGetConTimeAnalysis(@Param("conName") String conName,
                                               @Param("insList") List<String> authorityInsList,
                                               @Param("startTime") Date startTime,
                                               @Param("endTime") Date endTime);


}
