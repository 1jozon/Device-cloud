package cn.rmy.service;


import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.domain.ConditionsSelectReq;
import cn.rmy.domain.ReagentSurplusInfo;

import java.util.List;
import java.util.Map;

public interface ReagentSurplusInfoService {


    CommonResult selectAll();

    CommonResult selectById(int reagentSurplusInfoId);
    List<ReagentSurplusInfo> selectByDeviceIdAndReagentBoxId(String deviceId,String reagentBoxId);
    List<ReagentSurplusInfo> countSurplusByConditions(ConditionsSelectReq conditionsSelectReq);
    CommonResult emailSend(String deviceId);

    CommonResult insert(ReagentSurplusInfo reagentSurplusInfo);

//    CommonResult insertBatch(List<FaultCode> goodsList);

    CommonResult deletById(int reagentSurplusInfoId);

    CommonResult update(ReagentSurplusInfo reagentSurplusInfo);

//    CommonResult updateBatch(List<FaultCode> goodsList);

    CommonResult selectByConditions(Map<String, Object> map);

    CommonResult selectByPage(int current, int size, Map<String, Object> conditionsMap);

//    CommonResult receiveReagentSurplus(ReagentSurplusReq reagentSurplusReq);

//    CommonResult generateExcel(GenerateExcel generateExcel) throws IOException, ParseException;

}
