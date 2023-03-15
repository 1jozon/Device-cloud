package cn.rmy.controller;

import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.pojo.dto.CaliMsgDTO;
import cn.rmy.common.pojo.dto.ProjectSumUsedDto;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.mqttUtils.MQTTConnect;
import cn.rmy.service.Imp.CaliPointDataServiceImp;
import cn.rmy.service.Imp.ProjectServiceImp;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("rmy/caliTest")
public class TestCaliController {

    @Autowired
    private MQTTConnect mqttConnect;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CaliPointDataServiceImp caliPointDataService;

    @Autowired
    private ProjectServiceImp projectService;



/*    @RequestMapping("/time")
    public CommonResult test(){
        Date date = reagentBatchService.getCurrentTime();
        System.out.println(date);

        return CommonResult.success(date);
    }*/

    @RequestMapping("/getTestMsg")
    public CommonResult getTestMsg()throws MqttException {

        try {
            System.out.println("topic:reagentSurplus");
            mqttConnect.subscribe("reagentSurplus");

            System.out.println("topic:reagentMainCurve");
            mqttConnect.subscribe("reagentMainCurve");

            System.out.println("topic:CaliData");
            mqttConnect.subscribe("CaliData");

            System.out.println("topic:consumables");
            mqttConnect.subscribe("consumables");

            System.out.println("topic:fault");
            mqttConnect.subscribe("fault");

            System.out.println("topic:checkData");
            mqttConnect.subscribe("checkData");

            System.out.println("topic:qualityDat");
            mqttConnect.subscribe("qualityData");

            System.out.println("topic:GPSInfo");
            mqttConnect.subscribe("GPSInfo");


        }catch (MqttException e){
            e.getCause();
        }
        return CommonResult.success("成功");
    }

    /**
     * 定标缓存测试
     *
     * @return {@link CommonResult}
     */
    @RequestMapping("/getTestCaliCache")
    public CommonResult getCaliMsgInCache(){
        Set<String> keysList = new HashSet<>();
        boolean keys = false;
        List<CaliMsgDTO> caliMsgList = new ArrayList<>();

        try {
            keys = redisTemplate.hasKey("cali" + "*");
            keysList = redisTemplate.keys("cali" + "*");
            caliMsgList = redisTemplate.opsForValue().multiGet(keysList);

            for(CaliMsgDTO info : caliMsgList)
            {
                caliPointDataService.insertNewData(info);
                redisTemplate.delete("cali" + info.getCaliId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.success(caliMsgList);
    }

    @RequestMapping("/getInsProUsed")
    public CommonResult getInsProUsed(String insId){
        List<ProjectSumUsedDto> list = new ArrayList<>();
        list = projectService.getInsProjectUsed(insId);
        if(list == null){
            return CommonResult.error(CommonResultEm.ERROR, "未找到");
        }else{
            return CommonResult.success(list);
        }
    }

}
