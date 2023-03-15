package cn.rmy.service;


import cn.rmy.common.beans.faultManagement.FaultRecord;
import cn.rmy.common.beans.faultManagement.FaultRecordReq;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.dto.DrfDto;
import cn.rmy.dto.FrdDto;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FaultRecordService {


    List<FaultRecord> selectAll(String faultCode);

    CommonResult selectById(int faulyRecordId);
//    CommonResult selectByFaultCode(String faultCode);

    void insert(FaultRecordReq faultRecordReq);

//    CommonResult insertBatch(List<FaultRecord> goodsList);

    CommonResult deletById(int faulyRecordId);

    CommonResult update(FaultRecord faultRecord);

    CommonResult updateBatch(List<FaultRecord> faultRecordList);

    CommonResult selectByConditions(Map<String, Object> map);

//    CommonResult selectByPage1(int current,int size,FaultRecord faultRecord);
    CommonResult selectByPage2(int current, int size, Map<String, Object> conditionsMap);
//    CommonResult countFaultByConditions(Map<String, Object> conditionsMap);
    CommonResult countFaultForHandleStatus(Map<String,Object> conditionsMap);
    CommonResult emailSend(int faultRecordId) throws ParseException;

    List<DrfDto> countForFaultCode01(int nearlyDay, List<String> deviceList);
    List<DrfDto> countForFaultCode02(Date beginTime, Date endTime,List<String> deviceList);
    List<DrfDto> countForFaultCode1(String reagentNum, int nearlyDay, List<String> deviceList);
    List<DrfDto> countForFaultCode2(String reagentNum, Date beginTime, Date endTime,
                                  List<String> deviceList);
    List<DrfDto> countForFaultCode31(String reagentNum, String deviceId,int nearlyDay, List<String> deviceList);
    List<DrfDto> countForFaultCode32(String reagentNum, String deviceId, Date beginTime, Date endTime,
                                    List<String> deviceList);
    List<FrdDto> countForDevice1(String deviceId, int nearlyDay);

    List<FrdDto> countForDevice2(String deviceId, Date beginTime, Date endTime);


//    CommonResult generateExcel(GenerateExcel generateExcel) throws IOException, ParseException;

}
