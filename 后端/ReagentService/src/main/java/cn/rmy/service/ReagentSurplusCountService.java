package cn.rmy.service;


import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.domain.ReagentSurplusCount;
import cn.rmy.domain.RscDto;
import cn.rmy.domain.RsdDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReagentSurplusCountService{


    CommonResult selectAll();

    CommonResult selectById(int reagentSurplusCountId);
//    CommonResult selectByDeviceId(String deviceId);
    ReagentSurplusCount selectByBoxIdAndDeviceId(String reagentBoxId, String deviceId);
    List<Map<String,Object>> countForReagent(String reagentNum,int nearlyDay, Date... time);
    List<Map<String,Object>> countForDeviceId(String deviceId,int nearlyDay, Date... time);
    int sumReagentUseNumByDeviceId(String deviceId);
    List<String> sumReagentNum();
    CommonResult insert(ReagentSurplusCount reagentSurplusCount);

    List<RsdDto> countForReagent01(int nearlyDay, List<String> deviceList);
    List<RsdDto> countForReagent02(Date beginTime, Date endTime,List<String> deviceList);
    List<RsdDto> countForReagent1(String reagentNum, int nearlyDay, List<String> deviceList);
    List<RsdDto> countForReagent2(String reagentNum,Date beginTime, Date endTime,
                                 List<String> deviceList);
    List<RsdDto> countForReagent31(String reagentNum, String deviceId,int nearlyDay, List<String> deviceList);
    List<RsdDto> countForReagent32(String reagentNum,String deviceId,Date beginTime, Date endTime,
                                  List<String> deviceList);
    List<RscDto> countForDevice1(String deviceId,int nearlyDay);
    List<RscDto> countForDevice2(String deviceId, Date beginTime, Date endTime);


//    CommonResult insertBatch(List<FaultCode> goodsList);

    CommonResult deletById(int reagentSurplusCountId);

    CommonResult update(ReagentSurplusCount reagentSurplusCount);

//    CommonResult updateBatch(List<FaultCode> goodsList);

    CommonResult selectByConditions(Map<String, Object> map);

    CommonResult selectByPage(int current, int size, Map<String, Object> conditionsMap);

//    CommonResult receiveReagentSurplus(ReagentSurplusReq reagentSurplusReq);

//    CommonResult generateExcel(GenerateExcel generateExcel) throws IOException, ParseException;

}
