package cn.rmy.controller;

import cn.rmy.common.pojo.dto.*;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.service.Imp.ChartServiceImp;
import cn.rmy.service.imp.UsersServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 定标图表控制器
 *
 * @author chu
 * @date 2021/11/16
 */
@RestController
@RequestMapping("rmy/caliChart")
public class ChartController {

    @Autowired
    private ChartServiceImp chartService;

    @Autowired
    private UsersServiceImp usersService;


    /**
     * 定标分析
     *
     * @param condition 条件
     * @return {@link CommonResult}
     */
    @RequestMapping("/getCaliAnalysis")
    public CommonResult getAnalysis(@RequestBody CaliConditionDto condition){

        //用户权限仪器信息
        if (condition == null || condition.getUserId() == null || condition.getUserId().length() == 0){
            return CommonResult.error(CommonResultEm.ERROR, "请输入userId");
        }
        List<String> authorityInsList = usersService.getcurrentUserInsIdList(condition.getUserId());
        if(authorityInsList == null || authorityInsList.size() == 0){
            return CommonResult.success("用户无权查看相关数据");
        }
        CaliAnaDateRes caliAnaDateRes = new CaliAnaDateRes();
        caliAnaDateRes = chartService.getAnalysis(condition, authorityInsList);

        if(caliAnaDateRes != null && caliAnaDateRes.getTotal() > 0){
            return CommonResult.success(caliAnaDateRes);
        }
        return CommonResult.success("未找到相关定标数据");
    }
}
