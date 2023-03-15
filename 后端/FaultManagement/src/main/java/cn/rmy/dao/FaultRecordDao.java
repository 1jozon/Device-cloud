package cn.rmy.dao;

import cn.rmy.common.beans.faultManagement.FaultRecord;
import cn.rmy.dto.DrfDto;
import cn.rmy.dto.FrdDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface FaultRecordDao extends BaseMapper<FaultRecord> {
    Integer insertBatchSomeColumn(List<FaultRecord> faultRecords);

    Integer updateBatchSomeColumn(List<FaultRecord> faultRecords);

    List<DrfDto> countForFaultCode01(@Param("nearlyDay") int nearlyDay,
                                    @Param("deviceList")List<String> deviceList);

    List<DrfDto> countForFaultCode02(@Param("beginTime") Date beginTime,
                                    @Param("endTime") Date endTime,
                                    @Param("deviceList")List<String> deviceList);

    List<DrfDto> countForFaultCode1(@Param("faultCode") String faultCode,
                                      @Param("nearlyDay") int nearlyDay,
                                      @Param("deviceList")List<String> deviceList);

    List<DrfDto> countForFaultCode2(@Param("faultCode") String faultCode,
                                  @Param("beginTime") Date beginTime,
                                  @Param("endTime") Date endTime,
                                  @Param("deviceList")List<String> deviceList);

    List<DrfDto> countForFaultCode31(@Param("faultCode") String faultCode,
                                     @Param("deviceId") String deviceId,
                                    @Param("nearlyDay") int nearlyDay,
                                    @Param("deviceList")List<String> deviceList);

    List<DrfDto> countForFaultCode32(@Param("faultCode") String faultCode,
                                     @Param("deviceId") String deviceId,
                                    @Param("beginTime") Date beginTime,
                                    @Param("endTime") Date endTime,
                                    @Param("deviceList")List<String> deviceList);

    List<FrdDto> countForDevice1(@Param("deviceId") String deviceId,
                                 @Param("nearlyDay") int nearlyDay);

    List<FrdDto> countForDevice2(@Param("deviceId") String deviceId,
                                 @Param("beginTime") Date beginTime,
                                 @Param("endTime") Date endTime);

//    List<FaultRecordReq> selectFaultRecordByPage(Page page, @Param("faultRecord") FaultRecord faultRecord);
}
