package cn.rmy.controller;

import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.beans.faultManagement.FaultCode;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.redisUtils.CommonUtil;
import cn.rmy.common.redisUtils.DownLoad;
import cn.rmy.service.FaultCodeService;
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
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/rmy/faultCode")
public class FaultCodeController {

    private final static Logger logger = LoggerFactory.getLogger(FaultCodeController.class);

    @Value("${filePath}")
    private String filePath;

    @Autowired
    private FaultCodeService faultCodeService;

//    @Autowired
//    private RedisTemplate redisTemplate;


    @RequestMapping("/selectAll")
    public CommonResult selectAll(@RequestParam("faultCode") String faultCode){
        return CommonResult.success(faultCodeService.selectAll(faultCode));
    }


    @RequestMapping("/selectByFaultCode")
//    @HystrixCommand(fallbackMethod = "hystrixSelectById")
    public CommonResult selectByFaultCode(@RequestParam("faultCode") String faultCode){
//        return restTemplate.getForObject(USER_CONSUMER_PREFIX + "provider/user/selectById/"+id,User.class);

        FaultCode faultCode1 = faultCodeService.selectByFaultCode(faultCode);
        if(faultCode1 == null){
            return CommonResult.error(CommonResultEm.NOT_EXIST);
        }
        return CommonResult.success(faultCode1);
    }

    @RequestMapping("/insertFaultCode")
    public CommonResult insertFaultCode(@RequestBody FaultCode faultCode){
        return faultCodeService.insert(faultCode);
    }


    @RequestMapping("/updateFaultCode")
    public CommonResult updateFaultCode(@RequestBody FaultCode faultCode){
        return faultCodeService.update(faultCode);//  故障码不可修改
//        return faultCodeService.updateForRecorde(new FaultCodeReq());  故障码可修改
    }



    @RequestMapping("/selectByPage/{current}/{size}")
    public CommonResult selectByPage(@PathVariable("current") int current,
                                     @PathVariable("size") int size,
                                     @RequestBody(required = false) Map<String,Object> conditionsMap){
        //  在开发的时候  有入参：当前页号，每页条目数（可有可无）
        return faultCodeService.selectByPage(current,size,conditionsMap);
    }

    @RequestMapping("/deleteByFaultCodeId")
        public CommonResult deleteById(@RequestParam("faultCodeId") int faultCodeId){
        return faultCodeService.deletById(faultCodeId);
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
    @RequestMapping("/generateFaultCodeExcel")
    public void generateFaultCodeExcel(@RequestBody(required = false) Map<String,Object> conditionsMap,
                                                 @Param("fileName") String fileName, HttpServletRequest request,
                                                 HttpServletResponse response) throws IOException {

        Object obj = faultCodeService.selectByConditions(conditionsMap).getObj();
        if(CommonUtil.isNull(obj)){
            return;
        }
        List<FaultCode> list = JSONArray.parseArray(JSONArray.toJSONString(obj)).toJavaList(FaultCode.class);
        ListToExcel.faultCodeToExcel(filePath,fileName,list);

        DownLoad.downloadFile(filePath,fileName,request,response);
//        return CommonResult.success();
    }


    @RequestMapping("/generateFaultCodeCsv")
    public void generateFaultCodeCsv(@RequestBody(required = false) Map<String,Object> conditionsMap,
                                               @Param("fileName") String fileName, HttpServletRequest request,
                                               HttpServletResponse response) throws IOException {

        Object obj = faultCodeService.selectByConditions(conditionsMap).getObj();
        if(CommonUtil.isNull(obj)){
            return;
        }
        List<FaultCode> list = JSONArray.parseArray(JSONArray.toJSONString(obj)).toJavaList(FaultCode.class);
        ListToExcel.faultCodeToCsv(filePath,fileName,list);

        DownLoad.downloadFile(filePath,fileName,request,response);
//        return CommonResult.success();
    }
}
