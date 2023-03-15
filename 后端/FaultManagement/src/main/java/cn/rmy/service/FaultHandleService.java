package cn.rmy.service;



import cn.rmy.common.beans.faultManagement.FaultHandle;
import cn.rmy.common.redisUtils.CommonResult;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface FaultHandleService {


    List<FaultHandle> selectAll(String faultCode);

    CommonResult selectById(int faultHandleId);
//    CommonResult selectByFaultCode(String faultCode);

    CommonResult insert(FaultHandle faultHandle);

//    CommonResult insertBatch(List<FaultHandle> faultHandles);

    CommonResult deletById(int faultHandleId);

    CommonResult update(FaultHandle faultHandle);

    CommonResult updateBatch(List<FaultHandle> faultHandleList);

    CommonResult selectByConditions(Map<String, Object> map);

    CommonResult selectByPage2(int current, int size, Map<String, Object> conditionsMap);

    CommonResult emailSend(int faultRecordId) throws ParseException;

//    CommonResult generateExcel(GenerateExcel generateExcel) throws IOException, ParseException;

}
