package cn.rmy.service;



import cn.rmy.common.beans.faultManagement.FaultCode;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.dto.FaultCodeReq;

import java.util.List;
import java.util.Map;

public interface FaultCodeService{


    List<FaultCode> selectAll(String faultCode);

    CommonResult selectById(int faultCodeId);
    FaultCode selectByFaultCode(String faultCode);

    CommonResult insert(FaultCode faultCode);

    CommonResult insertBatch(List<FaultCode> faultCodeList);

    CommonResult deletById(int faultCodeId);

    CommonResult update(FaultCode faultCode);
    CommonResult updateForRecorde(FaultCodeReq faultCodeReq);

    CommonResult updateBatch(List<FaultCode> faultCodeList);

    CommonResult selectByConditions(Map<String, Object> map);

    CommonResult selectByPage(int current, int size, Map<String, Object> conditionsMap);

//    CommonResult generateExcel(GenerateExcel generateExcel) throws IOException, ParseException;

}
