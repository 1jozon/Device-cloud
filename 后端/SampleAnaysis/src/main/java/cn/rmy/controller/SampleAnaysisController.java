package cn.rmy.controller;

import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.dto.SampleAnalysisResDto;
import cn.rmy.common.dto.SampleConditionDto;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.service.imp.SampleAnalysisServiceImp;
import cn.rmy.service.imp.UsersServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 样品分析控制器
 *
 * @author chu
 * @date 2022/04/04
 */
@RestController
@RequestMapping("/rmy/sampleAnalysis")
public class SampleAnaysisController {

    @Autowired
    private UsersServiceImp usersService;

    @Autowired
    private SampleAnalysisServiceImp sampleAnalysisService;

    /**
     * 样本分析
     *
     * @param condition 条件
     * @return {@link CommonResult}
     */
    @RequestMapping("/getSampleAnalysis")
    public CommonResult getSampleAnalysis(@RequestBody SampleConditionDto condition){

        if(condition == null || condition.getUserId() == null || condition.getUserId().length() == 0){
            return CommonResult.error(CommonResultEm.ERROR, "请输入用户ID");
        }

        List<String> authorityInsIds = usersService.getcurrentUserInsIdList(condition.getUserId());
        if(authorityInsIds == null || authorityInsIds.size() == 0){
            return CommonResult.error(CommonResultEm.ERROR,"您无权查看任何仪器信息");
        }

        SampleAnalysisResDto sampleAnalysisResult = new SampleAnalysisResDto();
        if(condition.getProjectId() != null && condition.getProjectId().length() > 0){
            //根据项目年龄段统计
            sampleAnalysisResult = sampleAnalysisService.getSampleAgeAnalysis(condition, authorityInsIds);
        }else if(condition.getProType() == 1){
            //根据仪器进行项目分类
            sampleAnalysisResult = sampleAnalysisService.getSampleProAnalysis(condition, authorityInsIds);
        }else{
            //仪器分类
            sampleAnalysisResult = sampleAnalysisService.getSampleInsAnalysis(condition, authorityInsIds);
        }
        if(sampleAnalysisResult != null && sampleAnalysisResult.getTotal() != null && sampleAnalysisResult.getTotal() != 0){
            return CommonResult.success(sampleAnalysisResult);
        }else{
            return CommonResult.success("未找到相关统计结果");
        }
    }
}
