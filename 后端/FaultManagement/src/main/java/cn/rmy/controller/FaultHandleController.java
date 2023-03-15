package cn.rmy.controller;

import cn.rmy.common.beans.faultManagement.FaultHandle;
import cn.rmy.common.beans.faultManagement.FaultHandleReq;
import cn.rmy.common.beans.faultManagement.FaultRecord;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.service.FaultHandleService;
import cn.rmy.service.FaultRecordService;
import cn.rmy.service.InstrumentService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;


@RestController
@RequestMapping("/rmy/faultHandle")
public class FaultHandleController {

    private final static Logger logger = LoggerFactory.getLogger(FaultHandleController.class);

    @Value("${filePath}")
    private String filePath;

    @Autowired
    private FaultRecordService faultRecordService;

    @Autowired
    private FaultHandleService faultHandleService;

    @Autowired
    private InstrumentService instrumentService;

//    @Autowired
//    private RedisTemplate redisTemplate;


    @RequestMapping("/selectAll")
    public CommonResult selectAll(String faultCode){
        System.out.println("hello--- ");

        return CommonResult.success(faultHandleService.selectAll(faultCode));
    }

//    @RequestMapping("/selectById/{id}")
//    public User selectById(@PathVariable("id") Long id){
//        return userService.selectById(id);
//    }

//    public User hystrixSelectById(@PathVariable("id") Long id){
//        return new User().setId(id).setName("no name");
//    }


    /**
     * 处理故障   相当于   详情、设置按钮
     * @param faultHandleReq
     * @return
     */
    @RequestMapping("/handleFault")
    public CommonResult handleFault(@RequestBody FaultHandleReq faultHandleReq) throws ParseException {
        FaultRecord faultRecord = JSONObject.parseObject(JSONObject
                .toJSONString(faultRecordService.selectById(faultHandleReq.getFaultRecordId()).getObj())
                , FaultRecord.class);
        if(faultHandleReq.getIsHandle().equals("1")) {
            faultRecord.setHandleStatus("已处理");
            faultRecordService.update(faultRecord);
            instrumentService.setFaultStatus(faultRecord.getDeviceId(),1);
            FaultHandle faultHandle = new FaultHandle();
            faultHandle.setUserName(faultHandleReq.getUserName())
                    .setHandleTime(faultHandleReq.getHandleTime())
                    .setHandleAdvice(faultHandleReq.getHandleAdvice())
                    .setFaultRecordId(faultHandleReq.getFaultRecordId())
                    .setDeviceId(faultRecord.getDeviceId())
                    .setDeviceType(faultRecord.getDeviceType())
                    .setFaultCode(faultRecord.getFaultCode())
                    .setFaultDescribe(faultRecord.getFaultDescribe())
                    .setFaultAdvice(faultRecord.getFaultAdvice())
//                .setFaultType(faultRecord.getFaultType())
                    .setModuleCode(faultRecord.getModuleCode())
                    .setFaultClass(faultRecord.getFaultClass())
                    .setFaultTime(faultRecord.getFaultTime())
                    .setHandleStatus(faultRecord.getHandleStatus());
            faultHandleService.insert(faultHandle);
            faultHandleService.emailSend(faultHandleReq.getFaultRecordId());
        }
        return CommonResult.success();
    }


    @RequestMapping("/updateFaultHandle")
    public CommonResult updateFaultHandle(@RequestBody FaultHandle faultHandle){
//        id = 1374590216223924226L;
        return faultHandleService.update(faultHandle);
    }

    /**
     * 查询处理信息
     * @param faultRecordId
     * @return
     */
    @RequestMapping("/selectByfaultRecordId")
    public CommonResult selectByfaultRecordId(@RequestParam("faultRecordId") int faultRecordId){
//        id = 1374590216223924226L;
        return faultHandleService.selectById(faultRecordId);
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


    @RequestMapping("/selectByPage")
    public CommonResult selectByPage(@PathVariable("current") int current,
                                      @PathVariable("size") int size,
                                      @RequestBody(required = false) Map<String,Object> conditionsMap){
        //  在开发的时候  有入参：当前页号，每页条目数（可有可无）
        return faultHandleService.selectByPage2(current,size,conditionsMap);
    }


    @RequestMapping("/deleteById")
        public CommonResult deleteById(@RequestParam("faultHandleId") int faultHandleId){
        return faultHandleService.deletById(faultHandleId);
    }

//     ---------导出报表--------------

//    /**
//     *
//     * @param conditionsMap   条件封装成对象
//     * @param fileName
//     * @return
//     * @throws IOException
//     * @throws ParseException
//     */
//    @RequestMapping("/generateFaultHandleExcel")
//    public CommonResult generateFaultHandleExcel(@RequestBody(required = false) Map<String,Object> conditionsMap,
//                                                 @Param("fileName") String fileName, HttpServletRequest request,
//                                                 HttpServletResponse response) throws IOException {
//
//        Object obj = faultHandleService.selectByConditions(conditionsMap).getObj();
//        if(null == obj){
//            return CommonResult.error(CommonResultEm.ERROR,"记录为空,无需导出报表");
//        }
//        List<FaultHandle> list = JSONArray.parseArray(JSONArray.toJSONString(obj)).toJavaList(FaultHandle.class);
//        ListToExcel.faultHandleToExcel(resource,fileName,list);
//
//        DownLoad.downloadFile(resource,fileName,request,response);
//        return CommonResult.success();
////        GenerateExcel generateExcel = new GenerateExcel(list,outUrl,fileName);
////        return goodsService.generateExcel(generateExcel);
//    }

    //    ----------模板下载------------------
//    @RequestMapping("/downLoadGoodsApplyTemplate")
//    public void downLoadTemplate(HttpServletRequest request,
//                                 HttpServletResponse response) throws IOException {
//        String fileName = "goodsTemplate.xls";
//        ListToExcel.goodsToExcel(resource,fileName,null);
//        DownLoad.downloadFile(resource,fileName,request,response);
//    }

}
