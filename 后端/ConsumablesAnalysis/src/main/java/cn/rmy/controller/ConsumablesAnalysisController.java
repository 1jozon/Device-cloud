package cn.rmy.controller;

import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.dto.ConAnalysisResDto;
import cn.rmy.common.dto.ConsumablesConditionDto;
import cn.rmy.common.dto.ConsumablesDataDto;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.service.imp.ConAnalysisServiceImp;
import cn.rmy.service.imp.ConsumableServiceImp;
import cn.rmy.service.imp.UsersServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rmy/consumableAnalysis")
public class ConsumablesAnalysisController {

    @Autowired
    private ConAnalysisServiceImp conAnalysisService;

    @Autowired
    private ConsumableServiceImp consumableService;

    @Autowired
    private UsersServiceImp usersService;

    /**
     * 插入的数据
     *
     * @param data 数据
     * @return {@link CommonResult}
     */
    @RequestMapping("/insertConData")
    private CommonResult insertConData(@RequestBody ConsumablesDataDto data){
        if (data == null){
            return CommonResult.error(CommonResultEm.ERROR,"添加数据为空");
        }
        int rec = consumableService.insertData(data);

        if (rec != 1){
            return CommonResult.error(CommonResultEm.ERROR,"添加失败");
        }
        return CommonResult.success("添加成功");
    }

    /**
     * consumable条件的分析
     *
     * @param condition 条件
     * @return {@link CommonResult}
     */
    @RequestMapping("/getConAnalysisByCondition")
    public CommonResult ConByCondition(@RequestBody ConsumablesConditionDto condition){

        //用户权限仪器信息
        if (condition == null || condition.getUserId() == null || condition.getUserId().length() == 0){
            return CommonResult.error(CommonResultEm.ERROR, "请输入userId");
        }
        List<String> authorityInsList = usersService.getcurrentUserInsIdList(condition.getUserId());
        if(authorityInsList == null || authorityInsList.size() == 0){
            return CommonResult.success("用户无权查看相关数据");
        }
        ConAnalysisResDto conAnalysisRes = new ConAnalysisResDto();
        //List<ConAnalysisDto> conAnalysisList = new ArrayList<>();
        if (condition.getConsumName() != null && condition.getConsumName().length() != 0){
            //耗材名称搜索
            conAnalysisRes = conAnalysisService.getConAnalysis(condition, authorityInsList);
        }else{
            //仪器搜索
            conAnalysisRes = conAnalysisService.getInsAnalysis(condition, authorityInsList);
        }
        if (conAnalysisRes != null && conAnalysisRes.getTotal() != 0){
            return CommonResult.success(conAnalysisRes);
        }
        return CommonResult.success("未找到符合条件耗材分析数据");
    }

    /**
     * 得到所有耗材类型
     *
     * @return {@link CommonResult}
     */
    @RequestMapping("/getAllConTypes")
    public CommonResult getAllConsumTypes(){

        List<String> typeList = consumableService.getAllConsumType();
        if(typeList == null){
            return CommonResult.error(CommonResultEm.ERROR,"不存在耗材信息");
        }else{
            return CommonResult.success(typeList);
        }
    }

}
