package cn.rmy.dao.dtoDao;

import cn.rmy.common.beans.Instrument;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GetInstByCondition extends BaseMapper<Instrument> {


    @Select("select distinct a.* " +
            "from tb_instrument AS a, tb_user AS b, user_to_group AS c, group_to_group AS d, instrument_to_group AS e " +
            "where b.user_id=#{userId} " +
            "AND b.id=c.us_id " +
            "AND d.us_group_id=c.group_id " +
            "AND e.group_id=d.inst_group_id " +
            "AND e.group_id=#{groupId} " +
            "AND a.id=e.inst_id " +
            "AND a.deleted=0 AND b.deleted=0 AND c.deleted=0 AND d.deleted=0 AND e.deleted=0 " +
            "AND a.instrument_id like #{instrumentId} " +
            "AND a.instrument_model like #{instrumentModel} " +
            "AND a.instrument_address like #{instrumentAddress} " +
            "AND a.online_status like #{onlineStatus} " +
            "AND a.fault_status like #{faultStatus} " +
            "AND a.hospital_name like #{hospitalName} " +
            "order by a.instrument_date desc")
    List<Instrument> getInst(@Param("instrumentId") String instrumentId,
                                           @Param("instrumentModel") String instrumentModel,
                                            @Param("instrumentAddress") String instrumentAddress,
                                           @Param("onlineStatus") String onlineStatus,
                                           @Param("faultStatus") String faultStatus,
                                           @Param("hospitalName") String hospitalName,
                                           @Param("groupId") int groupId,
                                           @Param("userId") String userId);


    @Select("select distinct a.* " +
            "from tb_instrument AS a, tb_user AS b, user_to_group AS c, group_to_group AS d, instrument_to_group AS e " +
            "where b.user_id=#{userId} " +
            "AND b.id=c.us_id " +
            "AND d.us_group_id=c.group_id " +
            "AND e.group_id=d.inst_group_id " +
            "AND a.id=e.inst_id " +
            "AND a.deleted=0 AND b.deleted=0 AND c.deleted=0 AND d.deleted=0 AND e.deleted=0 " +
            "AND a.instrument_id like #{instrumentId} " +
            "AND a.instrument_model like #{instrumentModel} " +
            "AND a.instrument_address like #{instrumentAddress} " +
            "AND a.online_status like #{onlineStatus} " +
            "AND a.fault_status like #{faultStatus} " +
            "AND a.hospital_name like #{hospitalName} " +
            "order by a.instrument_date desc")
    List<Instrument> getInstWithoutGroupId(@Param("instrumentId") String instrumentId,
                             @Param("instrumentModel") String instrumentModel,
                             @Param("instrumentAddress") String instrumentAddress,
                             @Param("onlineStatus") String onlineStatus,
                             @Param("faultStatus") String faultStatus,
                             @Param("hospitalName") String hospitalName,
                             @Param("userId") String userId);


    @Select("select distinct a.* " +
            "from tb_instrument AS a, instrument_to_group AS e " +
            "where e.group_id=#{groupId} " +
            "AND a.id=e.inst_id " +
            "AND a.deleted=0 AND e.deleted=0 " +
            "AND a.instrument_id like #{instrumentId} " +
            "AND a.instrument_model like #{instrumentModel} " +
            "AND a.instrument_address like #{instrumentAddress} " +
            "AND a.online_status like #{onlineStatus} " +
            "AND a.fault_status like #{faultStatus} " +
            "AND a.hospital_name like #{hospitalName} " +
            "order by a.instrument_date desc")
    List<Instrument> getInstWithoutUserId(@Param("instrumentId") String instrumentId,
                             @Param("instrumentModel") String instrumentModel,
                             @Param("instrumentAddress") String instrumentAddress,
                             @Param("onlineStatus") String onlineStatus,
                             @Param("faultStatus") String faultStatus,
                             @Param("hospitalName") String hospitalName,
                             @Param("groupId") int groupId);

    @Select("select distinct a.* " +
            "from tb_instrument AS a " +
            "where a.deleted=0 " +
            "AND a.instrument_id like #{instrumentId} " +
            "AND a.instrument_model like #{instrumentModel} " +
            "AND a.instrument_address like #{instrumentAddress} " +
            "AND a.online_status like #{onlineStatus} " +
            "AND a.fault_status like #{faultStatus} " +
            "AND a.hospital_name like #{hospitalName} " +
            "order by a.instrument_date desc")
    List<Instrument> getInstWithoutAll(@Param("instrumentId") String instrumentId,
                                          @Param("instrumentModel") String instrumentModel,
                                          @Param("instrumentAddress") String instrumentAddress,
                                          @Param("onlineStatus") String onlineStatus,
                                          @Param("faultStatus") String faultStatus,
                                          @Param("hospitalName") String hospitalName);

}

















