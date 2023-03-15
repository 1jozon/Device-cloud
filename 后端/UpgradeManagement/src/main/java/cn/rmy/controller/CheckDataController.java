package cn.rmy.controller;


import cn.rmy.beans.SelectResult;
import cn.rmy.beans.dto.CheckDataDto;
import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.beans.checkData.CheckData;
import cn.rmy.common.beans.checkData.CheckDataVO;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.redisUtils.DownLoad;
import cn.rmy.service.CheckDataService;
import cn.rmy.service.InstrumentModelVOService;
import cn.rmy.utils.CheckDataListToExcel;
import lombok.extern.slf4j.Slf4j;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("rmy/checkData")
public class CheckDataController {

    @Value("${filePath}")
    private String filePath;

    @Autowired
    private CheckDataService checkDataService;

    @Autowired
    private InstrumentModelVOService instrumentModelService;

    @RequestMapping("/updateCheckData")
    public CommonResult updateCheckData(@RequestBody CheckData checkData){
        if(checkData.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"未返回id");
        int rec = checkDataService.update(checkData);
        if(rec == -1) return CommonResult.error(CommonResultEm.ERROR,"当前id的检验记录不存在");
        return CommonResult.success("更新成功");
    }

    @RequestMapping("/deleteCheckData")
    public CommonResult deleteCheckData(@RequestBody CheckData checkData){
        if(checkData.getId()==0) return CommonResult.error(CommonResultEm.ERROR,"未返回id");
        int rec = checkDataService.delete(checkData);
        if(rec<=0) return CommonResult.error(CommonResultEm.ERROR,"删除失败");
        return CommonResult.success("删除成功");
    }

    @RequestMapping("/getCheckDataByCondition/{current}/{size}")
    public CommonResult getCheckDataByCondition(@PathVariable("current") int current,@PathVariable("size") int size, @RequestBody CheckDataVO checkDataVO) throws ParseException {
        CheckDataDto checkDataDto = new CheckDataDto();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
        String startTime;
        String endTime;
        if(checkDataVO.getStartTime()==null)
            startTime = "2000-12-1 00:00:00";
        else startTime = checkDataVO.getStartTime();
        if(checkDataVO.getEndTime()==null)
            endTime = "2100-12-1 00:00:00";
        else endTime = checkDataVO.getEndTime();
        checkDataDto.setInstrumentId(checkDataVO.getInstrumentId());
        checkDataDto.setProjectName(checkDataVO.getProjectName());
        checkDataDto.setPatientSex(checkDataVO.getPatientSex());

        if(checkDataVO.getStartTime()!=null&&checkDataVO.getStartTime().length()>0) checkDataDto.setCreateTime(ft.parse(checkDataVO.getStartTime()));
        if(checkDataVO.getEndTime()!=null&&checkDataVO.getEndTime().length()>0) checkDataDto.setUpdateTime(ft.parse(checkDataVO.getEndTime()));
        if(checkDataVO.getUserId()==null||checkDataVO.getUserId().length()==0) return CommonResult.error(CommonResultEm.ERROR,"未输入userId");
        SelectResult selectResult = checkDataService.getByCondition(checkDataDto,current,size,startTime,endTime,checkDataVO.getUserId());
        //System.out.println(checkData);
        //if(selectResult.getList()==null||selectResult.getTotal()==0) return CommonResult.error(CommonResultEm.ERROR,"未查询到检验记录");
        if(selectResult.getList()==null||selectResult.getTotal()==0) return CommonResult.success("未查询到检验记录");
        return CommonResult.success(selectResult);
    }

    @RequestMapping("/getCheckDataByConditionManager/{current}/{size}")
    public CommonResult getCheckDataByConditionManager(@PathVariable("current") int current,@PathVariable("size") int size, @RequestBody CheckDataVO checkDataVO) throws ParseException {
        CheckDataDto checkDataDto = new CheckDataDto();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
        String startTime;
        String endTime;
        if(checkDataVO.getStartTime()==null)
            startTime = "2000-12-1 00:00:00";
        else startTime = checkDataVO.getStartTime();
        if(checkDataVO.getEndTime()==null)
            endTime = "2100-12-1 00:00:00";
        else endTime = checkDataVO.getEndTime();
        checkDataDto.setInstrumentId(checkDataVO.getInstrumentId());
        checkDataDto.setProjectName(checkDataVO.getProjectName());
        checkDataDto.setPatientSex(checkDataVO.getPatientSex());

        if(checkDataVO.getStartTime()!=null&&checkDataVO.getStartTime().length()>0) checkDataDto.setCreateTime(ft.parse(checkDataVO.getStartTime()));
        if(checkDataVO.getEndTime()!=null&&checkDataVO.getEndTime().length()>0) checkDataDto.setUpdateTime(ft.parse(checkDataVO.getEndTime()));

        SelectResult selectResult = checkDataService.getByCondition(checkDataDto,current,size,startTime,endTime);
        //System.out.println(checkData);
        //if(selectResult.getList()==null||selectResult.getTotal()==0) return CommonResult.error(CommonResultEm.ERROR,"未查询到检验记录");
        if(selectResult.getList()==null||selectResult.getTotal()==0) return CommonResult.success("未查询到检验记录");
        return CommonResult.success(selectResult);
    }

    @RequestMapping("/getCheckDataFile")
    public void getCheckDataFile(@RequestBody CheckDataVO checkDataVO, HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        CheckDataDto checkDataDto = new CheckDataDto();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        checkDataDto.setInstrumentId(checkDataVO.getInstrumentId())
                .setProjectName(checkDataVO.getProjectName())
                .setPatientSex(checkDataVO.getPatientSex());
        String startTime;
        String endTime;
/*        if(checkDataVO.getStartTime()==null)
            startTime = "2000-12-1 00:00:00";
        else startTime = ft.format(checkDataVO.getStartTime());
        if(checkDataVO.getEndTime()==null)
            endTime = "2100-12-1 00:00:00";
        else endTime = ft.format(checkDataVO.getEndTime());*/
        //下载默认最近7天的数据
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if(checkDataVO.getStartTime()==null){
            //startTime = "2000-12-1 00:00:00";
            calendar.add(Calendar.DATE, -7);
            Date d = calendar.getTime();
            startTime  = ft.format(d).toString();
        }
        else {
            startTime = checkDataVO.getStartTime();
        }
        if(checkDataVO.getEndTime()==null){
            //endTime = "2100-12-1 00:00:00";
            calendar.add(Calendar.DATE, 7);
            Date d = calendar.getTime();
            endTime = ft.format(d).toString();
        }
        else {
            endTime = checkDataVO.getEndTime();
        }

        if(checkDataVO.getStartTime()!=null&&checkDataVO.getStartTime().length()>0) checkDataDto.setCreateTime(ft.parse(checkDataVO.getStartTime()));
        if(checkDataVO.getEndTime()!=null&&checkDataVO.getEndTime().length()>0) checkDataDto.setUpdateTime(ft.parse(checkDataVO.getEndTime()));
        if(checkDataVO.getUserId()==null||checkDataVO.getUserId().length()==0) checkDataVO.setUserId("null");
        List<CheckDataDto> list = checkDataService.getByCondition(checkDataDto,startTime,endTime,checkDataVO.getUserId());
        String fileName = "checkData.xlsx";
        Map<Integer,String> map = instrumentModelService.getTypeName();

        CheckDataListToExcel.checkDataToExcel(filePath,fileName,list,map);
        DownLoad.downloadFile(filePath,fileName,request,response);

        //System.out.println(checkData);
        //if(selectResult.getList()==null||selectResult.getTotal()==0) return CommonResult.error(CommonResultEm.ERROR,"未查询到检验记录");
        //return CommonResult.success(selectResult);
    }

    @RequestMapping("/getCheckDataFileManager")
    public void getCheckDataFileManager(@RequestBody CheckDataVO checkDataVO, HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        CheckDataDto checkDataDto = new CheckDataDto();
        //SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        checkDataDto.setInstrumentId(checkDataVO.getInstrumentId())
                .setProjectName(checkDataVO.getProjectName())
                .setPatientSex(checkDataVO.getPatientSex());
        String startTime;
        String endTime;
        //下载默认最近7天的数据
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if(checkDataVO.getStartTime()==null){
            //startTime = "2000-12-1 00:00:00";
            calendar.add(Calendar.DATE, -7);
            Date d = calendar.getTime();
            startTime  = ft.format(d).toString();
        }
        else {
            startTime = checkDataVO.getStartTime();
        }
        if(checkDataVO.getEndTime()==null){
            //endTime = "2100-12-1 00:00:00";
            calendar.add(Calendar.DATE, 7);
            Date d = calendar.getTime();
            endTime = ft.format(d).toString();
        }
        else {
            endTime = checkDataVO.getEndTime();
        }

        if(checkDataVO.getStartTime()!=null&&checkDataVO.getStartTime().length()>0) checkDataDto.setCreateTime(ft.parse(checkDataVO.getStartTime()));
        if(checkDataVO.getEndTime()!=null&&checkDataVO.getEndTime().length()>0) checkDataDto.setUpdateTime(ft.parse(checkDataVO.getEndTime()));

        List<CheckDataDto> list = checkDataService.getByCondition(checkDataDto,startTime,endTime);
        String fileName = "checkData.xlsx";
        Map<Integer,String> map = instrumentModelService.getTypeName();

        CheckDataListToExcel.checkDataToExcel(filePath,fileName,list,map);
        DownLoad.downloadFile(filePath,fileName,request,response);

        //System.out.println(checkData);
        //if(selectResult.getList()==null||selectResult.getTotal()==0) return CommonResult.error(CommonResultEm.ERROR,"未查询到检验记录");
        //return CommonResult.success(selectResult);
    }

}
