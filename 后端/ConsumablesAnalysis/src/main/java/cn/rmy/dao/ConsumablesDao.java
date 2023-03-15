package cn.rmy.dao;

import cn.rmy.common.beans.analysis.ConsumablesDataInfo;
import cn.rmy.common.dto.ConAnalysisDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface ConsumablesDao extends BaseMapper<ConsumablesDataInfo> {

    /**
     * 得到所有类型
     *
     * @return {@link List}<{@link String}>
     */
    @Select("select a.con_name " +
            "from consumables_data as a " +
            "group by a.con_name")
    List<String> getAlltype();




    /**
     * 分析 仪器-不规定时间
     *
     * @param insId ins id
     * @return {@link List}<{@link ConAnalysisDto}>
     */
    @Select("select a.con_name AS objectName, sum(a.vol_used) as amount " +
            "from consumables_data AS a " +
            "where a.solid_state = #{solidState} " +
            "and a.ins_id = #{insId} " +
            "group by a.con_name " +
            "order by amount DESC " +
            "limit 5")
    List<ConAnalysisDto> conGetInsAnalysis(@Param("solidState") int solidState,
                                           @Param("insId") String insId);

    /**
     * 分析 仪器-规定时间
     *
     * @param insId     ins id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List}<{@link ConAnalysisDto}>
     */
    @Select("select a.con_name AS objectName, sum(a.vol_used) as amount " +
            "from consumables_data AS a " +
            "where a.solid_state = #{solidState} " +
            "and a.ins_id = #{insId} " +
            "and a.date_time between #{startTime} and #{endTime} " +
            "group by a.con_name " +
            "order by amount DESC " +
            "limit 5")
    List<ConAnalysisDto> conGetInsTimeAnalysis(@Param("insId") String insId,
                                               @Param("solidState") int solidState,
                                               @Param("startTime")Date startTime,
                                               @Param("endTime") Date endTime);




}
