package cn.rmy.controller;

import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.domain.ReagentMainCurve;
import cn.rmy.service.ReagentMainCurveService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * 一句话功能描述.
 * 项目名称:  试剂卡信息
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:   xsc
 * 创建时间:  2021.11.13 10点40分
 */
@Component
@RestController
@RequestMapping("/rmy/reagentMainCurve")
//@RabbitListener(queues = "rmy.queue.$share/g1/reagentMainCurve")
@RabbitListener(queues = "rmy.queue.$share/g2/reagentMainCurve")
public class ReagentMainCurveController {

    @Autowired
    private ReagentMainCurveService reagentMainCurveService;



    @RequestMapping("/selectAll")
    public CommonResult selectAll(){
        return reagentMainCurveService.selectAll();
    }


    @RequestMapping("/insertReagentMainCurve")
    public CommonResult insertReagentMainCurve(@RequestBody ReagentMainCurve reagentMainCurve){
        return reagentMainCurveService.insert(reagentMainCurve);
    }

    @RabbitHandler
    public void insertReagentMainCurve2(String message) throws ParseException {
        ReagentMainCurve reagentMainCurve = new ReagentMainCurve();
        JSONObject obj = JSONObject.parseObject(message);
        reagentMainCurve.setDeviceId(obj.get("ID").toString())
                .setReagentBatchId(obj.get("ReagentBatchID").toString())
                .setRlu1(Integer.valueOf(obj.get("RLU1").toString()))
                .setRlu2(Integer.valueOf(obj.get("RLU2").toString()))
                .setRlu3(Integer.valueOf(obj.get("RLU3").toString()))
                .setRlu4(Integer.valueOf(obj.get("RLU4").toString()))
                .setRlu5(Integer.valueOf(obj.get("RLU5").toString()))
                .setRlu6(Integer.valueOf(obj.get("RLU6").toString()))
                .setRlu7(Integer.valueOf(obj.get("RLU7").toString()))
                .setRlu8(Integer.valueOf(obj.get("RLU8").toString()))
                .setRlu9(Integer.valueOf(obj.get("RLU9").toString()))
                .setRlu10(Integer.valueOf(obj.get("RLU10").toString()))
                .setConc1(Double.valueOf(obj.get("Conc1").toString()))
                .setConc2(Double.valueOf(obj.get("Conc2").toString()))
                .setConc3(Double.valueOf(obj.get("Conc3").toString()))
                .setConc4(Double.valueOf(obj.get("Conc4").toString()))
                .setConc5(Double.valueOf(obj.get("Conc5").toString()))
                .setConc6(Double.valueOf(obj.get("Conc6").toString()))
                .setConc7(Double.valueOf(obj.get("Conc7").toString()))
                .setConc8(Double.valueOf(obj.get("Conc8").toString()))
                .setConc9(Double.valueOf(obj.get("Conc9").toString()))
                .setConc10(Double.valueOf(obj.get("Conc10").toString()))
                .setCurveType(Integer.valueOf(obj.get("CurveType").toString()))
                .setParamA(Double.valueOf(obj.get("ParamA").toString()))
                .setParamB(Double.valueOf(obj.get("ParamB").toString()))
                .setParamC(Double.valueOf(obj.get("ParamC").toString()))
                .setParamD(Double.valueOf(obj.get("ParamD").toString()))
                .setParamE(Double.valueOf(obj.get("ParamE").toString()))
                .setTotalNum(Integer.valueOf(obj.get("TotalNum").toString()))
                .setCaliNum(Integer.valueOf(obj.get("CaliNum").toString()))
                .setEffectiveTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String) obj.get("EffectiveTime")))
                .setNsb0(Double.valueOf(obj.get("NSB0").toString()))
                .setRluMax0(Integer.valueOf(obj.get("RLUmax0").toString()))
                .setProCon(Double.valueOf(obj.get("proCon").toString()))
                .setProRlu(Double.valueOf(obj.get("proRLU").toString()))
                .setDiluRatio(Double.valueOf(obj.get("DiluRatio").toString()))
                .setGetTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String) obj.get("getTime")));

        System.out.println(reagentMainCurve);
        reagentMainCurveService.insert(reagentMainCurve);
        return ;
    }

    @RequestMapping("/deleteByReagentMainCurveId")
    public CommonResult deleteByReagentMainCurveId(@RequestParam("reagentMainCurveId") int reagentMainCurveId){
        return reagentMainCurveService.deletById(reagentMainCurveId);
    }

    @RequestMapping("/updateReagentMainCurveId")
    public CommonResult updateReagentMainCurveId(@RequestBody ReagentMainCurve reagentMainCurve){
        return reagentMainCurveService.update(reagentMainCurve);
    }

    @RequestMapping("/selectByPage/{current}/{size}")
    public CommonResult selectByPage(@PathVariable("current") int current,
                                     @PathVariable("size") int size,
                                     @RequestBody(required = false) Map<String,Object> conditionsMap){
        //  在开发的时候  有入参：当前页号，每页条目数（可有可无）
        return reagentMainCurveService.selectByPage(current,size,conditionsMap);
    }

}
