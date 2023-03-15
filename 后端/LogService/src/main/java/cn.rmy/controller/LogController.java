package cn.rmy.controller;

import cn.rmy.common.beans.faultManagement.Log;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.redisUtils.CommonUtil;
import cn.rmy.common.redisUtils.DownLoad;
import cn.rmy.service.LogService;
import cn.rmy.util.ListToExcel;
import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 一句话功能描述.
 * 项目名称:  日志管理
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:
 * 创建时间:
 */
@RestController
@RequestMapping("/rmy/log")
public class LogController {

    private final static Logger logger = LoggerFactory.getLogger(LogController.class);

    @Value("${filePath}")
    private String filePath;

    @Autowired
    private LogService logService;



    @RequestMapping("/selectByPage/{current}/{size}")
    public CommonResult selectByPage(@PathVariable("current") int current,
                                     @PathVariable("size") int size,
                                     @RequestBody(required = false) Map<String,Object> conditionsMap){
        //  在开发的时候  有入参：当前页号，每页条目数（可有可无）
        return logService.selectByPage(current,size,conditionsMap);
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
    @RequestMapping("/generateLogExcel")
    public void generateLogExcel(@RequestBody(required = false) Map<String,Object> conditionsMap,
                                               @Param("fileName") String fileName, HttpServletRequest request,
                                               HttpServletResponse response) throws IOException {

        Object obj = logService.selectByConditions(conditionsMap).getObj();
        if(CommonUtil.isNull(obj)){
            return;
        }
        List<Log> list = JSONArray.parseArray(JSONArray.toJSONString(obj)).toJavaList(Log.class);
        ListToExcel.logToExcel(filePath,fileName,list);

        DownLoad.downloadFile(filePath,fileName,request,response);
//        return CommonResult.success();
    }


    @RequestMapping("/generateLogCsv")
    public void generateLogCsv(@RequestBody(required = false) Map<String,Object> conditionsMap,
                                             @Param("fileName") String fileName, HttpServletRequest request,
                                             HttpServletResponse response) throws IOException {

        Object obj = logService.selectByConditions(conditionsMap).getObj();
        if(CommonUtil.isNull(obj)){
            return;
        }
        List<Log> list = JSONArray.parseArray(JSONArray.toJSONString(obj)).toJavaList(Log.class);
        ListToExcel.logToCsv(filePath,fileName,list);

        DownLoad.downloadFile(filePath,fileName,request,response);
//        return CommonResult.success();
    }

}
