package cn.rmy.controller;

import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.pojo.dto.*;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.redisUtils.CommonUtil;
import cn.rmy.service.UsersService;
import cn.rmy.service.imp.AnalysisServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 质控统计分析控制器
 *
 * @author chu
 * @date 2021/12/21
 */
@RestController
@RequestMapping("/rmy/QualityAnalysis")
public class AnalysisController {

    @Autowired
    private AnalysisServiceImp analysisService;

    @Autowired
    private UsersService usersService;

    /**
     * 统计条件分析-指定仪器
     *
     * @return {@link CommonResult}
     */
    @RequestMapping("/getInsAnalysisByCondition")
    public CommonResult getInsAnalysisByCondition(@RequestBody QualityConditionDto condition){
        if(condition.getUserId() == null){
            return CommonResult.error(CommonResultEm.REQ_PARAM_IS_ERROR,"请输入useId");
        }
        List<String> authorithInsList = usersService.getcurrentUserInsIdList(condition.getUserId());
        if (authorithInsList == null || authorithInsList.size() == 0){
            return CommonResult.error(CommonResultEm.SUCCESS, "您无权查看仪器相关信息");
        }
        QualityAnaInsRes qualityAnaInsRes = new QualityAnaInsRes();
        qualityAnaInsRes = analysisService.qualityInsAnalysis(condition, authorithInsList);
        if (qualityAnaInsRes != null && qualityAnaInsRes.getTotal() != 0){
            return CommonResult.success(qualityAnaInsRes);
        }

        return CommonResult.success("未查询到相关质控数据");
    }

    /**
     * 质控统计分析-指定项目名称
     *
     * @param condition 条件
     * @return {@link CommonResult}
     */
    @RequestMapping("/getProAnalysisByCondition")
    public CommonResult getProAnalysisByCondition(@RequestBody QualityConditionDto condition){

        if(condition.getUserId() == null){
            return CommonResult.error(CommonResultEm.REQ_PARAM_IS_ERROR,"请输入useId");
        }
        List<String> authorithInsList = usersService.getcurrentUserInsIdList(condition.getUserId());
        if (authorithInsList == null || authorithInsList.size() == 0){
            return CommonResult.error(CommonResultEm.SUCCESS, "您无权查看仪器相关信息");
        }
        QualityAnaProRes qualityAnaProRes = new QualityAnaProRes();
        if (CommonUtil.isNotNull(condition.getProjectName()) && condition.getProjectName().length() != 0){
            qualityAnaProRes = analysisService.qualityProAnalysis(condition,authorithInsList);
            if (qualityAnaProRes != null && qualityAnaProRes.getTotal() > 0){
                return CommonResult.success(qualityAnaProRes);
            }else{
                return CommonResult.success("未查询到相关数据");
            }
        }else{
            return CommonResult.error(CommonResultEm.ERROR,"请选择项目相关条件");
        }
    }

    /**
     * 所有项目名称
     *
     * @return {@link CommonResult}
     */
    @RequestMapping("/getAllProjectName")
    public CommonResult getAllProjectName(@RequestBody QualityConditionDto conditionDto){
        List<ProjectDataDto> projectNames = new ArrayList<>();
        projectNames = analysisService.getAllProjectNames(conditionDto.getProjectName());
        if (projectNames == null || projectNames.size() == 0){
            return CommonResult.error(CommonResultEm.ERROR,"未找到项目名称");
        }
        return CommonResult.success(projectNames);
    }

}
