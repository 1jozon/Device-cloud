package cn.rmy.controller;

import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.beans.analysis.ProjectInfo;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.redisUtils.CommonUtil;
import cn.rmy.domain.*;
import cn.rmy.service.*;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RestController
@RequestMapping("/rmy/reagentSurplus")
//@RabbitListener(queues = "rmy.queue.$share/g1/reagentSurplus")
@RabbitListener(queues = "rmy.queue.$share/g2/reagentSurplus")
public class ReagentSurplusController {

    private final static Logger logger = LoggerFactory.getLogger(ReagentSurplusController.class);


    @Autowired
    private ReagentSurplusCountService reagentSurplusCountService;

    @Autowired
    private ReagentSurplusInfoService reagentSurplusInfoService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UsersService usersService;


//    @Autowired
//    private RedisTemplate redisTemplate;


    //     ---------接受试剂余量信息操作--------------

//    @RequestMapping("/receiveReagentSurplus")
    @RabbitHandler
    public void receiveReagentSurplus(String message) throws ParseException {

        JSONObject obj = JSONObject.parseObject(message);
        if (CommonUtil.isNull(obj)) {
            logger.error("试剂余量信息为空");
            return;
        } else {
            String reagentBoxId = obj.get("ReagentBoxID").toString();//   盒号  唯一标识  但是有不同的仪器编号
            String reagentNum = reagentBoxId.substring(1,4); //  真正用于分类的；
            String reagentProj = reagentBoxId.substring(0,1);
            String deviceId = obj.get("ID").toString();         //   仪器编号
            String reagentSurplusStr = obj.get("reagentSurplus").toString();
            int reagentSurplus = Integer.valueOf(reagentSurplusStr);   //  试剂余量
            Date getTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(obj.get("getTime").toString());      //  获取时间

            List<ReagentSurplusInfo> reagentSurplusInfoList = reagentSurplusInfoService.selectByDeviceIdAndReagentBoxId(deviceId,reagentBoxId);
            int size = reagentSurplusInfoList.size();
            if (0 == size || reagentSurplusInfoList.isEmpty()) {
                // 余量表、使用表  新增行
                ReagentSurplusInfo tempReagentSurplusInfo = new ReagentSurplusInfo();
                tempReagentSurplusInfo.setReagentBoxId(reagentBoxId)
                        .setDeviceId(deviceId)
                        .setReagentNum(reagentNum)
                        .setReagentProj(reagentProj)
                        .setReagentSurplus(reagentSurplus)
                        .setGetTime(getTime);
                reagentSurplusInfoService.insert(tempReagentSurplusInfo);
            } else if(size>2){
                logger.error("试剂余量信息查询大小>2,错误");
                return;
            }else{
                ReagentSurplusInfo reagentSurplusInfo = reagentSurplusInfoList.get(0);
                int oldSurplus = reagentSurplusInfo.getReagentSurplus();
//                System.out.println("oldSurplus  " + oldSurplus);
                if (0 == oldSurplus) {
                    // 如果老余量为0  即为用光，则同样进行余量表的更新  不需要进行使用表的添加
                    reagentSurplusInfo.setReagentSurplus(reagentSurplus);
//                    ReagentSurplusInfo reagentSurplusInfo1 = new ReagentSurplusInfo();
//                    reagentSurplusInfo1.setReagentSurplusInfoId(reagentSurplusInfo.getReagentSurplusInfoId())
//                            .setDeviceId(deviceId)
//                            .setReagentNum(reagentNum)
//                            .setReagentProj(reagentProj)
//                            .setReagentBoxId(reagentBoxId)
//                            .setReagentSurplus(reagentSurplus);
                    reagentSurplusInfoService.update(reagentSurplusInfo);
                } else {
                    if (oldSurplus < reagentSurplus) {

                        CommonResult result = CommonResult.error(CommonResultEm.ERROR, "余量不减反增，出错");
                        /**
                         * 如果增加，抛异常，调用仪器接口，通知相应管理该的有权限的人
                         * .....  业务完成！！！！！！*/
                        reagentSurplusInfoService.emailSend(deviceId);
                        logger.error("余量错误",result);
                        return;
                    } else {
                        int newUseNum = oldSurplus - reagentSurplus;  //  使用量
//                        System.out.println("newUseNum" + newUseNum);
                        reagentSurplusInfo.setReagentSurplus(reagentSurplus);
                        reagentSurplusInfoService.update(reagentSurplusInfo);
                        ReagentSurplusCount reagentSurplusCount = reagentSurplusCountService
                                                                    .selectByBoxIdAndDeviceId(reagentBoxId, deviceId);
                        if (CommonUtil.isNull(reagentSurplusCount)) {
                            ReagentSurplusCount tempReagentSurplusCount = new ReagentSurplusCount();
                            tempReagentSurplusCount.setReagentBoxId(reagentBoxId)
                                    .setReagentNum(reagentNum)
                                    .setReagentProj(reagentProj)
                                    .setDeviceId(deviceId)
                                    .setReagentUseNum(newUseNum)
                                    .setGetTime(getTime);
                            reagentSurplusCountService.insert(tempReagentSurplusCount);
                        } else {
                            //更新
                            int oldUseNum = reagentSurplusCount.getReagentUseNum(); //  从使用表中获取老的使用量
//                            System.out.println("newUseNum" + newUseNum + "oldUseNum" + oldUseNum);
                            reagentSurplusCount.setReagentUseNum(oldUseNum + newUseNum);
                            reagentSurplusCountService.update(reagentSurplusCount);
                        }
                    }
                }

            }
        }
    }


    @RequestMapping("/countSurplusByConditions")
    public CommonResult countSurplusByConditions(@RequestBody ConditionsSelectReq conditionsSelectReq){
        List<ReagentSurplusInfo> result = reagentSurplusInfoService.countSurplusByConditions(conditionsSelectReq);
        return CommonResult.success(result);
    }
    // 根据条件进行统计查询，无分页   ——————  试剂使用量  正式使用
    @RequestMapping("/countUseNumByConditions")
    public CommonResult countUseNumByConditions(@RequestBody ConditionsSelectReq conditionsSelectReq){
        int countByReagentOrDevice = conditionsSelectReq.getCountByReagentOrDevice(); // 0：默认（哪些仪器消耗量最多）1:按试剂分类，2：按仪器分类 3：两个都传
        int nearlyDay = conditionsSelectReq.getNearlyDay();// 条件为：0：自定义（日期范围查询），1：最近一天，2：最近一周，3：最近一个月，4：最近三个月
        String reagentNum = conditionsSelectReq.getReagentNum(); // 1:按试剂分
        String deviceId = conditionsSelectReq.getDeviceId();// 2：按仪器分
//        List<Map<String,Object>> result = null;
        List<String> devicesList = usersService.getcurrentUserInsIdList(conditionsSelectReq.getUserId());
        if (devicesList == null || devicesList.size() == 0){
            return CommonResult.error(CommonResultEm.SUCCESS, "您无权查看仪器相关信息");
        }
        if(CommonUtil.isNull(countByReagentOrDevice) || countByReagentOrDevice == 1){
            List<RsdDto> rsdDtos;
            if(nearlyDay != 0){
                rsdDtos = reagentSurplusCountService
                        .countForReagent1(reagentNum, nearlyDay, devicesList);
                System.out.println("rsdDtos ------->  " + rsdDtos);
            }else {
                Date beginTime = conditionsSelectReq.getBeginTime();
                Date endTime = conditionsSelectReq.getEndTime();
                rsdDtos = reagentSurplusCountService
                        .countForReagent2(reagentNum, beginTime, endTime, devicesList);
                System.out.println("rsdDtos ------->  " + rsdDtos);

            }
            return CommonResult.success(rsdDtos);
        }else if(countByReagentOrDevice == 2){
            List<RscDto> rscDtos;
            if(nearlyDay != 0){
                rscDtos = reagentSurplusCountService.countForDevice1(deviceId, nearlyDay);
            }else {
                Date beginTime = conditionsSelectReq.getBeginTime();
                Date endTime = conditionsSelectReq.getEndTime();
                rscDtos = reagentSurplusCountService.countForDevice2(deviceId,beginTime,endTime);

            }
            List<RscDto2> rscDto2s = new ArrayList<>();
            for(RscDto rscDto:rscDtos){
                RscDto2 rscDto2 = new RscDto2();
                ProjectInfo projectExist = projectService.getProjectExist(rscDto.getReagentNum());
                if(CommonUtil.isNull(projectExist)){
                    rscDto2.setReagentProjName(rscDto.getReagentNum());
                }else {
                    rscDto2.setReagentProjName(projectExist.getProjectName());
                }
                rscDto2.setReagentUseNum(rscDto.getReagentUseNum());
                rscDto2s.add(rscDto2);
            }
            return CommonResult.success(rscDto2s);
        }else if(countByReagentOrDevice == 0){// 默认按照仪器，哪个使用量最多
                List<RsdDto> rsdDtos;
                if(nearlyDay != 0){
                    rsdDtos = reagentSurplusCountService
                            .countForReagent01(nearlyDay, devicesList);
                    System.out.println("rsdDtos ------->  " + rsdDtos);
                }else {
                    Date beginTime = conditionsSelectReq.getBeginTime();
                    Date endTime = conditionsSelectReq.getEndTime();
                    rsdDtos = reagentSurplusCountService
                            .countForReagent02(beginTime, endTime, devicesList);
                    System.out.println("rsdDtos ------->  " + rsdDtos);

                }
            return CommonResult.success(rsdDtos);

        }else{// 3 两个都传
            List<RsdDto> rsdDtos;
            if(nearlyDay != 0){
                rsdDtos = reagentSurplusCountService
                        .countForReagent31(reagentNum,deviceId,nearlyDay, devicesList);
                System.out.println("rsdDtos ------->  " + rsdDtos);
            }else {
                Date beginTime = conditionsSelectReq.getBeginTime();
                Date endTime = conditionsSelectReq.getEndTime();
                rsdDtos = reagentSurplusCountService
                        .countForReagent32(reagentNum,deviceId,beginTime, endTime, devicesList);
                System.out.println("rsdDtos ------->  " + rsdDtos);

            }
            return CommonResult.success(rsdDtos);
        }
    }


    /**

    //     ---------按试剂统计使用量操作--------------

//    @RequestMapping("/countByReagent")
    public CommonResult countByReagent() throws InvocationTargetException, IllegalAccessException {
        List<Map<String,Object>> result = reagentSurplusCountService.countForReagent();
        if(CommonUtil.isNull(result)){
            return CommonResult.error();
        }
        // 实现Map covert to Bean  目的：前台tap时可以显示详细信息
        for(Map<String,Object> map :result){
            RscDto rscDto = new RscDto();
            for(Map.Entry<String,Object> entry:map.entrySet()){
                String key = entry.getKey();
                if(key.equals("reagent_box_id")){
                    rscDto.setReagent_box_id((String) entry.getValue());
                }
                if(key.equals("sum(reagent_use_num)")){
                    rscDto.setReagent_use_num(entry.getValue());
                }
            }
            System.out.println(rscDto);
        }

        return CommonResult.success(result);
    }


    //     ---------按仪器统计使用量操作--------------
//    @RequestMapping("/countByDevice")
    public CommonResult countByDevice(){
        List<Map<String,Object>> result = reagentSurplusCountService.countForDeviceId();
        if(CommonUtil.isNull(result)){
            return CommonResult.error();
        }
        // 实现Map covert to Bean  目的：前台tap时可以显示详细信息
        for(Map<String,Object> map :result){
            DscDto dscDto = new DscDto();
            for(Map.Entry<String,Object> entry:map.entrySet()){
                String key = entry.getKey();
                if(key.equals("device_id")){
                    dscDto.setDevice_id((String) entry.getValue());
                }
                if(key.equals("sum(reagent_use_num)")){
                    dscDto.setReagent_use_num(entry.getValue());
                }
            }
            System.out.println(dscDto);
        }

        return CommonResult.success(result);
    }
     */

//    @RequestMapping("/selectById/{id}")
//    public User selectById(@PathVariable("id") Long id){
//        return userService.selectById(id);
//    }

//    @RequestMapping("/selectByFaultCode")
////    @HystrixCommand(fallbackMethod = "hystrixSelectById")
//    public CommonResult selectByFaultCode(@RequestParam("faultCode") String faultCode){
////        return restTemplate.getForObject(USER_CONSUMER_PREFIX + "provider/user/selectById/"+id,User.class);
//
//        FaultCode faultCode1 = JSONObject.parseObject(JSONObject
//                .toJSONString(reagentSurplusService.selectByFaultCode(faultCode)),FaultCode.class);
//        if(faultCode1 == null){
//            return CommonResult.error(CommonResultEm.NOT_EXIST);
//        }
//        return CommonResult.success(faultCode1);
//    }



//    @RequestMapping("/selectByPage")
//    public CommonResult selectByPage(@PathVariable("current") int current,
//                                        @PathVariable("size") int size,
//                                        @RequestBody(required = false) Map<String,Object> conditionsMap){
//        //  在开发的时候  有入参：当前页号，每页条目数（可有可无）
//        return reagentSurplusService.selectByPage(current,size,conditionsMap);
//    }
//
//    @RequestMapping("/deleteByFaultCodeId")
//        public CommonResult deleteById(@RequestParam("reagentSurplusId") int reagentSurplusId){
//        return reagentSurplusService.deletById(reagentSurplusId);
//    }
}
