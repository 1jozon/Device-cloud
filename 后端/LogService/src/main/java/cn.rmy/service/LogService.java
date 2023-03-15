package cn.rmy.service;


import cn.rmy.common.beans.faultManagement.Log;
import cn.rmy.common.redisUtils.CommonResult;

import java.util.Map;

public interface LogService {


    CommonResult selectAll();

    CommonResult selectById(int logId);
//    CommonResult selectByOperateor(String operateor);
//    CommonResult selectByOperateType(String operateType);


    CommonResult insert(Log log);

//    CommonResult insertBatch(List<FaultCode> goodsList);

    CommonResult deletById(int logId);

//    CommonResult update(FaultCode goods);
//    CommonResult updateBatch(List<FaultCode> goodsList);

    CommonResult selectByConditions(Map<String, Object> map);

    CommonResult selectByPage(int current, int size, Map<String, Object> conditionsMap);

//    CommonResult generateExcel(GenerateExcel generateExcel) throws IOException, ParseException;

}
