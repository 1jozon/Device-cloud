package cn.rmy.controller;

import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.beans.faultManagement.FaultRecord;
import cn.rmy.common.redisUtils.*;
import cn.rmy.dto.ConditionsSelectReq;
import cn.rmy.dto.DrfDto;
import cn.rmy.dto.FrdDto;
import cn.rmy.service.FaultRecordService;
import cn.rmy.service.UsersService;
import cn.rmy.util.ListToExcel;
import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

//@Component
@RestController
@RequestMapping("/rmy/faultRecord")
//@RabbitListener(queues = "rmy.queue.fault")
public class FaultRecordController {

    private final static Logger logger = LoggerFactory.getLogger(FaultRecordController.class);

    @Value("${filePath}")
    private String filePath;

    @Autowired
    private FaultRecordService faultRecordService;
    @Autowired
    private UsersService usersService;

//    @Autowired
//    private RedisTemplate redisTemplate;


    @RequestMapping("/selectAll")
    public CommonResult selectAll(@RequestParam("faultCode") String faultCode){
        return CommonResult.success(faultRecordService.selectAll(faultCode));
    }

//    @RequestMapping("/selectById/{id}")
//    public User selectById(@PathVariable("id") Long id){
//        return userService.selectById(id);
//    }

//    public User hystrixSelectById(@PathVariable("id") Long id){
//        return new User().setId(id).setName("no name");
//    }

/*
    @RequestMapping("/insertFaultRecord")
    public CommonResult insertFaultRecord(@RequestBody FaultRecordReq faultRecordReq){
        return faultRecordService.insert(faultRecordReq);
    }

 */

    /*
    @RabbitHandler
    public void insertFaultRecord2(String message) throws ParseException {
        FaultRecordReq faultRecordReq = new FaultRecordReq();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject obj = JSONObject.parseObject(message);
        faultRecordReq.setDeviceId((String) obj.get("ID"))
                .setFaultClass((String)obj.get("faultClass"))
                .setFaultCode((String)obj.get("faultCode"))
                .setModuleCode((String)obj.get("moduleCode"))
                .setFaultTime(ft.parse(obj.getString("faultTime")));
        logger.info("信息：",faultRecordReq);

        faultRecordService.insert(faultRecordReq);
    }
    */



    @RequestMapping("/updateFaultRecord")
    public CommonResult updateFaultRecord(@RequestBody FaultRecord faultRecord){
//        id = 1374590216223924226L;
        return faultRecordService.update(faultRecord);
    }

//    @RequestMapping("/selectByConditions")
//    public List<User> selectUserByConditions(Map<String,Object> map){
//        List<User> list = userService.selectByConditions(map);
//        return list;
//    }

//    @RequestMapping("/selectByPage1")
//    public CommonResult selectByPage1(@PathVariable("current") int current,
//                                      @PathVariable("size") int size,
//                                      @RequestBody FaultRecord faultRecord){
//        //  在开发的时候  有入参：当前页号，每页条目数（可有可无）
//        return faultRecordService.selectByPage1(current,size,faultRecord);
//    }


    @RequestMapping("/selectByPage/{current}/{size}")
    public CommonResult selectByPage(@PathVariable("current") int current,
                                      @PathVariable("size") int size,
                                      @RequestBody(required = false) Map<String,Object> conditionsMap){
        //  在开发的时候  有入参：当前页号，每页条目数（可有可无）
        return faultRecordService.selectByPage2(current,size,conditionsMap);
    }


    /**
     * 故障的条件统计   字段有：  deviceId(仪器名称，编号)  deviceType（仪器类型） handleStatus  beginTime  endTime nearlyDay
     *              需要定时？
     *
     * 故障统计：筛选条件（时间段、仪器名称、编号、状态）
     * 需求1：统计时间段内各个仪器的故障数量，及总故障状态（已解决、未解决）的数量变化
     * 需求2：统计时间段内所有仪器中各个故障码出现的故障数量变化
     * @param conditionsSelectReq
     * @return
     */
//    @RequestMapping("/countFaultByConditions")
//    public CommonResult countFaultByConditions(@RequestBody Map<String,Object> conditionsMap){
//        //  在开发的时候  有入参：当前页号，每页条目数（可有可无）
//        if (null == conditionsMap || 0 == conditionsMap.size()) {
//            return CommonResult.error(CommonResultEm.ERROR,"统计条件为空，查询出错");
//        }
//        if(!conditionsMap.containsKey("statisticalDemand")){
//            return CommonResult.error(CommonResultEm.ERROR,"统计需求为空，查询出错");
//        }
//
//        return faultRecordService.countFaultByConditions(conditionsMap);
//
//    }

    @RequestMapping("/countFaultByConditions")
    public CommonResult countFaultByConditions(@RequestBody ConditionsSelectReq conditionsSelectReq){
        int countByDeviceOrFaultCode = conditionsSelectReq.getCountByDeviceOrFaultCode(); // 1:按仪器分类，2：按故障码分类
        int nearlyDay = conditionsSelectReq.getNearlyDay();// 条件为：0：自定义（日期范围查询），1：最近一天，2：最近一周，3：最近一个月，4：最近三个月
        String deviceId = conditionsSelectReq.getDeviceId();// 1：按仪器
        String faultCode = conditionsSelectReq.getFaultCode(); // 2:按故障码分

//        List<Map<String,Object>> result = null;
        List<String> devicesList = usersService.getcurrentUserInsIdList(conditionsSelectReq.getUserId());
        if (devicesList == null || devicesList.size() == 0){
            return CommonResult.error(CommonResultEm.SUCCESS, "您无权查看仪器相关信息");
        }
        if(CommonUtil.isNull(countByDeviceOrFaultCode) || countByDeviceOrFaultCode == 1){//按仪器
            List<FrdDto> frdDtos;
            if(nearlyDay != 0){
                frdDtos = faultRecordService
                        .countForDevice1(deviceId, nearlyDay);
                System.out.println("frdDtos ------->  " + frdDtos);
            }else {
                Date beginTime = conditionsSelectReq.getBeginTime();
                Date endTime = conditionsSelectReq.getEndTime();
                frdDtos = faultRecordService
                        .countForDevice2(deviceId, beginTime, endTime);
                System.out.println("frdDtos ------->  " + frdDtos);

            }
            return CommonResult.success(frdDtos);
        }else if(countByDeviceOrFaultCode == 2){// 按故障码
            List<DrfDto> drfDtos;
            if(nearlyDay != 0){
                drfDtos = faultRecordService.countForFaultCode1(faultCode, nearlyDay,devicesList);
            }else {
                Date beginTime = conditionsSelectReq.getBeginTime();
                Date endTime = conditionsSelectReq.getEndTime();
                drfDtos = faultRecordService.countForFaultCode2(faultCode,beginTime,endTime,devicesList);

            }
            return CommonResult.success(drfDtos);
        }else if(countByDeviceOrFaultCode == 0){// 默认
            List<DrfDto> drfDtos;
            if(nearlyDay != 0){
                drfDtos = faultRecordService.countForFaultCode01(nearlyDay,devicesList);
            }else {
                Date beginTime = conditionsSelectReq.getBeginTime();
                Date endTime = conditionsSelectReq.getEndTime();
                drfDtos = faultRecordService.countForFaultCode02(beginTime,endTime,devicesList);

            }
            return CommonResult.success(drfDtos);
        }else{//两个条件都用
            List<DrfDto> drfDtos;
            if(nearlyDay != 0){
                drfDtos = faultRecordService.countForFaultCode31(faultCode, deviceId,nearlyDay,devicesList);
            }else {
                Date beginTime = conditionsSelectReq.getBeginTime();
                Date endTime = conditionsSelectReq.getEndTime();
                drfDtos = faultRecordService.countForFaultCode32(faultCode,deviceId,beginTime,endTime,devicesList);

            }
            return CommonResult.success(drfDtos);
        }
    }








    @RequestMapping("/countFaultForHandleStatus")
    public CommonResult countFaultForHandleStatus(@RequestBody Map<String,Object> conditionsMap){
        // 包含 deviceIdList   beginTime endTime
        //  在开发的时候  有入参：当前页号，每页条目数（可有可无）
        if (null == conditionsMap || 0 == conditionsMap.size()) {
            return CommonResult.error(CommonResultEm.ERROR,"统计条件为空，查询出错");
        }
        if(!conditionsMap.containsKey("countByDeviceOrFaultCode")){
            return CommonResult.error(CommonResultEm.ERROR,"统计需求为空，查询出错");
        }
        return faultRecordService.countFaultForHandleStatus(conditionsMap);
    }






    @LogAnno(operateType = "删除故障记录")
    @RequestMapping("/deleteById")
    public CommonResult deleteById(@RequestParam("faultRecordId") int faultRecordId){
        return faultRecordService.deletById(faultRecordId);
    }

//     ---------导出报表--------------

    /**
     *
     * @param conditionsMap   条件封装成对象
     * @param fileName
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping("/generateFaultRecordExcel")
    public void generateFaultRecordExcel(@RequestBody(required = false) Map<String,Object> conditionsMap,
                                                 @Param("fileName") String fileName, HttpServletRequest request,
                                                 HttpServletResponse response) throws IOException {

        Object obj = faultRecordService.selectByConditions(conditionsMap).getObj();
        if(CommonUtil.isNull(obj)){
            return;
        }
        List<FaultRecord> list = JSONArray.parseArray(JSONArray.toJSONString(obj)).toJavaList(FaultRecord.class);
        ListToExcel.faultRecordsToExcel(filePath,fileName,list);

        DownLoad.downloadFile(filePath,fileName,request,response);
//        return CommonResult.success();
//        GenerateExcel generateExcel = new GenerateExcel(list,outUrl,fileName);
//        return goodsService.generateExcel(generateExcel);
    }


    @RequestMapping("/generateFaultRecordCsv")
    public void generateFaultRecordCsv(@RequestBody(required = false) Map<String,Object> conditionsMap,
                                                 @Param("fileName") String fileName, HttpServletRequest request,
                                                 HttpServletResponse response) throws IOException {

        Object obj = faultRecordService.selectByConditions(conditionsMap).getObj();
        if(CommonUtil.isNull(obj)){
            return;
        }
        List<FaultRecord> list = JSONArray.parseArray(JSONArray.toJSONString(obj)).toJavaList(FaultRecord.class);
        ListToExcel.faultRecordsToCsv(filePath,fileName,list);

        DownLoad.downloadFile(filePath,fileName,request,response);
//        return CommonResult.success();
//        GenerateExcel generateExcel = new GenerateExcel(list,outUrl,fileName);
//        return goodsService.generateExcel(generateExcel);
    }

    //    ----------模板下载------------------
//    @RequestMapping("/downLoadGoodsApplyTemplate")
//    public void downLoadTemplate(HttpServletRequest request,
//                                 HttpServletResponse response) throws IOException {
//        String fileName = "goodsTemplate.xls";
//        ListToExcel.goodsToExcel(resource,fileName,null);
//        DownLoad.downloadFile(resource,fileName,request,response);
//    }


    /**
     * 邮箱提醒
     * @param faultRecordId
     * @return
     */
    @RequestMapping("/emailSend")
    public CommonResult emailSend (@RequestParam("faultRecordId") int faultRecordId) throws ParseException {
        /**
         * 邮箱发送  将这个故障
         *              根据   仪器所在地区、 该地区由谁分管、故障级别是否需要发送
         *              定时发送
         */

        return faultRecordService.emailSend(faultRecordId);

    }

    @RequestMapping("/updateBatch")
    public CommonResult updateBatch(@RequestBody List<FaultRecord> faultRecordList){
        /**
         * 邮箱发送  将这个故障
         *              根据   仪器所在地区、 该地区由谁分管、故障级别是否需要发送
         *              定时发送
         */

        return faultRecordService.updateBatch(faultRecordList);

    }
}
