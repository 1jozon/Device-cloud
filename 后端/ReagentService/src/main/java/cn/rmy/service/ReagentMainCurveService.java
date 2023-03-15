package cn.rmy.service;


import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.redisUtils.LogAnno;
import cn.rmy.domain.ReagentMainCurve;

import java.util.Map;

public interface ReagentMainCurveService {


    CommonResult selectAll();

    CommonResult selectById(int reagentMainCurveId);

    @LogAnno(operateType = "试剂卡信息插入")
    CommonResult insert(ReagentMainCurve reagentMainCurve);


    CommonResult deletById(int reagentMainCurveId);

    CommonResult update(ReagentMainCurve reagentMainCurve);


    CommonResult selectByConditions(Map<String, Object> map);

    CommonResult selectByPage(int current, int size, Map<String, Object> conditionsMap);

//    CommonResult receiveReagentSurplus(ReagentSurplusReq reagentSurplusReq);

//    CommonResult generateExcel(GenerateExcel generateExcel) throws IOException, ParseException;

}
