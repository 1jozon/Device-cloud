package cn.rmy.controller;

import cn.rmy.common.pojo.dto.CaliConditionDto;
import cn.rmy.common.pojo.dto.SelectResult;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.service.Imp.FormServiceImp;
import cn.rmy.service.imp.UsersServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 定标表单控制器
 *
 * @author chu
 * @date 2021/11/16
 */
@RestController
@RequestMapping("rmy/caliForm")
public class FormController {

    @Autowired
    private FormServiceImp formService;

    @Autowired
    private UsersServiceImp usersService;


    /**
     * 条件获取定标列表-y
     *
     * @param condition 条件
     * @return {@link CommonResult}
     */
    @RequestMapping("/getCaliListByCondition/{current}/{size}")
    public CommonResult getCaliListByCondition(@PathVariable int current,
                                               @PathVariable int size,
                                               @RequestBody CaliConditionDto condition){

        if(condition.getUserId() == null){
            return CommonResult.error(CommonResultEm.REQ_PARAM_IS_ERROR,"请输入useId");
        }
        List<String> authorithInsList = usersService.getcurrentUserInsIdList(condition.getUserId());
        if (authorithInsList == null || authorithInsList.size() == 0){
            return CommonResult.error(CommonResultEm.SUCCESS, "您无权查看仪器相关信息");
        }

        SelectResult selectResult = formService.getCaliListByCondition(current,size,condition, authorithInsList);
        if (selectResult.getTotal() == 0) {
            return CommonResult.success("未找到相关结果");
        }else{
            return CommonResult.success(selectResult);
        }
    }

}
