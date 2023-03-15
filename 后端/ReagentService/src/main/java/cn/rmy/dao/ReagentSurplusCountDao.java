package cn.rmy.dao;

import cn.rmy.domain.ReagentSurplusCount;
import cn.rmy.domain.RscDto;
import cn.rmy.domain.RsdDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ReagentSurplusCountDao extends BaseMapper<ReagentSurplusCount> {

    List<RsdDto> countForReagent01(@Param("nearlyDay") int nearlyDay,
                                  @Param("deviceList")List<String> deviceList);

    List<RsdDto> countForReagent02(@Param("beginTime") Date beginTime,
                                  @Param("endTime") Date endTime,
                                  @Param("deviceList")List<String> deviceList);

    List<RsdDto> countForReagent1(@Param("reagentNum") String reagentNum,
                       @Param("nearlyDay") int nearlyDay,
                       @Param("deviceList")List<String> deviceList);

    List<RsdDto> countForReagent2(@Param("reagentNum") String reagentNum,
                       @Param("beginTime") Date beginTime,
                       @Param("endTime") Date endTime,
                       @Param("deviceList")List<String> deviceList);

    List<RscDto> countForDevice1(@Param("deviceId") String deviceId,
                                 @Param("nearlyDay") int nearlyDay);

    List<RscDto> countForDevice2(@Param("deviceId") String deviceId,
                                  @Param("beginTime") Date beginTime,
                                  @Param("endTime") Date endTime);

    List<RsdDto> countForReagent31(@Param("reagentNum") String reagentNum,
                                  @Param("deviceId") String deviceId,
                                  @Param("nearlyDay") int nearlyDay,
                                  @Param("deviceList")List<String> deviceList);

    List<RsdDto> countForReagent32(@Param("reagentNum") String reagentNum,
                                  @Param("deviceId") String deviceId,
                                  @Param("beginTime") Date beginTime,
                                  @Param("endTime") Date endTime,
                                  @Param("deviceList")List<String> deviceList);

    Integer insertBatchSomeColumn(List<ReagentSurplusCount> reagentSurplusCounts);

    Integer updateBatchSomeColumn(List<ReagentSurplusCount> reagentSurplusCounts);
}
