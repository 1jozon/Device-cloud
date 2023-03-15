package cn.rmy.service;


import cn.rmy.common.beans.reagent.ReagentSurplusCountVO;
import cn.rmy.common.redisUtils.CommonResult;


import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReagentSurplusCountVOService {


    CommonResult selectAll();

    CommonResult selectById(int reagentSurplusCountId);
//    CommonResult selectByDeviceId(String deviceId);
    ReagentSurplusCountVO selectByBoxIdAndDeviceId(String reagentBoxId, String deviceId);
    List<Map<String,Object>> countForReagent(int nearlyDay, Date... time);
    List<Map<String,Object>> countForDeviceId(int nearlyDay, Date... time);
    String sumReagentUseNumByDeviceId(String deviceId);
    List<String> sumReagentNum();
    CommonResult insert(ReagentSurplusCountVO reagentSurplusCount);

//    CommonResult insertBatch(List<FaultCode> goodsList);

    CommonResult deletById(int reagentSurplusCountId);

    CommonResult update(ReagentSurplusCountVO reagentSurplusCount);

//    CommonResult updateBatch(List<FaultCode> goodsList);

    CommonResult selectByConditions(Map<String, Object> map);

 //   CommonResult selectByPage(int current, int size, Map<String, Object> conditionsMap);

//    CommonResult receiveReagentSurplus(ReagentSurplusReq reagentSurplusReq);

//    CommonResult generateExcel(GenerateExcel generateExcel) throws IOException, ParseException;

}
