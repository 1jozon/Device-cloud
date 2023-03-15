package cn.rmy.dao;

import cn.rmy.beans.dto.CheckDataDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CheckDataDtoDao extends BaseMapper<CheckDataDto> {

    @Select("select distinct b.id,b.instrument_id,b.model_id,b.patient_age,b.patient_sex,b.patient_name," +
            "b.patient_area,a.project_name,b.test_rlu,b.test_result,b.reference_range,b.unit,b.dilu_ratio," +
            "b.reagent_batch_id,b.sample_type,b.exception,b.check_time,b.create_time,b.update_time " +
            "from tb_project AS a, tb_check AS b, tb_user AS c,  user_to_group AS d, instrument_to_group AS e, tb_instrument AS f, group_to_group AS g " +
            "where b.deleted=0 AND c.deleted=0 AND d.deleted=0 AND e.deleted=0 AND f.deleted=0 AND g.deleted=0 " +
            "AND c.user_id = #{userId} " +
            "AND c.id = d.us_id " +
            "AND d.group_id = g.us_group_id " +
            "AND g.inst_group_id = e.group_id " +
            "AND e.inst_id = f.id " +
            "AND f.instrument_id = b.instrument_id " +
            "AND a.project_name like #{projectName} " +
            "AND b.instrument_id like #{instrumentId} " +
            "AND b.check_time > #{startTime} " +
            "AND b.check_time < #{endTime} " +
            "AND a.project_id = b.project_id " +
            "AND b.patient_sex = #{sex} " +
            "ORDER BY b.check_time Desc")
    List<CheckDataDto> getByIPSSEU(@Param("instrumentId") String instrumentId,
                                 @Param("projectName") String projectName,
                                 @Param("sex") int sex,
                                 @Param("startTime") String startTime,
                                 @Param("endTime") String endTime,
                                  @Param("userId") String userId);

    @Select("select distinct b.id,b.instrument_id,b.model_id,b.patient_age,b.patient_sex,b.patient_name," +
            "b.patient_area,a.project_name,b.test_rlu,b.test_result,b.reference_range,b.unit,b.dilu_ratio," +
            "b.reagent_batch_id,b.sample_type,b.exception,b.check_time,b.create_time,b.update_time " +
            "from tb_project AS a, tb_check AS b " +
            "where b.deleted=0 " +
            "AND a.project_name like #{projectName} " +
            "AND b.instrument_id like #{instrumentId} " +
            "AND b.check_time > #{startTime} " +
            "AND b.check_time < #{endTime} " +
            "AND a.project_id = b.project_id " +
            "AND b.patient_sex = #{sex} " +
            "ORDER BY b.check_time Desc")
    List<CheckDataDto> getByIPSSE(@Param("instrumentId") String instrumentId,
                                   @Param("projectName") String projectName,
                                   @Param("sex") int sex,
                                   @Param("startTime") String startTime,
                                   @Param("endTime") String endTime);

    @Select("select distinct b.id,b.instrument_id,b.model_id,b.patient_age,b.patient_sex,b.patient_name," +
            "b.patient_area,a.project_name,b.test_rlu,b.test_result,b.reference_range,b.unit,b.dilu_ratio," +
            "b.reagent_batch_id,b.sample_type,b.exception,b.check_time,b.create_time,b.update_time " +
            "from tb_project AS a, tb_check AS b, tb_user AS c,  user_to_group AS d, instrument_to_group AS e, tb_instrument AS f, group_to_group AS g " +
            "where b.deleted=0 AND c.deleted=0 AND d.deleted=0 AND e.deleted=0 AND f.deleted=0 AND g.deleted=0 " +
            "AND c.user_id = #{userId} " +
            "AND c.id = d.us_id " +
            "AND d.group_id = g.us_group_id " +
            "AND g.inst_group_id = e.group_id " +
            "AND e.inst_id = f.id " +
            "AND f.instrument_id = b.instrument_id " +
            "AND a.project_name like #{projectName} " +
            "AND b.instrument_id like #{instrumentId} " +
            "AND b.check_time > #{startTime} " +
            "AND b.check_time < #{endTime} " +
            "AND a.project_id = b.project_id " +
            "ORDER BY b.check_time Desc")
    List<CheckDataDto> getByIPSEU(@Param("instrumentId") String instrumentId,
                                 @Param("projectName") String projectName,
                                 @Param("startTime") String startTime,
                                 @Param("endTime") String endTime,
                                 @Param("userId") String userId);
    //没有涉及性别的

    @Select("select distinct b.id,b.instrument_id,b.model_id,b.patient_age,b.patient_sex,b.patient_name," +
            "b.patient_area,a.project_name,b.test_rlu,b.test_result,b.reference_range,b.unit,b.dilu_ratio," +
            "b.reagent_batch_id,b.sample_type,b.exception,b.check_time,b.create_time,b.update_time " +
            "from tb_project AS a, tb_check AS b " +
            "where b.deleted=0 " +
            "AND a.project_name like #{projectName} " +
            "AND b.instrument_id like #{instrumentId} " +
            "AND b.check_time > #{startTime} " +
            "AND b.check_time < #{endTime} " +
            "AND a.project_id = b.project_id " +
            "ORDER BY b.check_time Desc " +
            "LIMIT 50000")
    List<CheckDataDto> getByIPSE(@Param("instrumentId") String instrumentId,
                                  @Param("projectName") String projectName,
                                  @Param("startTime") String startTime,
                                  @Param("endTime") String endTime);


}
