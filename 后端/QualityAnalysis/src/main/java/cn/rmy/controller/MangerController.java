package cn.rmy.controller;

import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.beans.analysis.QualityDataInfo;
import cn.rmy.common.pojo.dto.QualityConditionDto;
import cn.rmy.common.pojo.dto.QualityDataDto;
import cn.rmy.common.pojo.dto.SelectResult;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.redisUtils.LogAnno;
import cn.rmy.service.imp.QualityServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理控制器
 *
 * @author chu
 * @date 2021/12/21
 */
@RestController
@RequestMapping("rmy/qualityManage")
public class MangerController {

    @Autowired
    private QualityServiceImp qualityService;

    /**
     * 插入
     *
     * @param qualityData 质量数据
     * @return {@link CommonResult}
     */
    @RequestMapping("/insert")
    public CommonResult insert(@RequestBody QualityDataDto qualityData){
        if (qualityData == null){
            return CommonResult.error(CommonResultEm.ERROR, "请输入正确质控数据信息");
        }
        int rec = qualityService.insertQualityInfo(qualityData);
        if (rec == 1){
            return CommonResult.success("添加新质控数据成功");
        }
        return CommonResult.error(CommonResultEm.ERROR,"添加失败");
    }

    /**
     * 修改更新
     *
     * @param qualityDataInfo 高质量的数据信息
     * @return {@link CommonResult}
     */
    @LogAnno(operateType = "更新质控结果")
    @RequestMapping("/updateQualityData")
    public CommonResult update(@RequestBody QualityDataInfo qualityDataInfo){
        if (qualityDataInfo.getId() <= 0
                ||qualityDataInfo.getQctrlResult() == null
                || qualityDataInfo.getQctrlResult().length() == 0){
            return CommonResult.error(CommonResultEm.ERROR,"修改失败，请输入正x`确质控数据");
        }
        int rec = qualityService.updateQualityInfo(qualityDataInfo);
        if (rec == 1){
            return CommonResult.success("修改质控数据信息成功");
        }
        return CommonResult.error(CommonResultEm.ERROR,"修改失败。");
    }


    /**
     * 多条件筛选质控数据
     *
     * @param condition 条件
     * @return {@link CommonResult}
     */
    @RequestMapping("/getQualityByConditions/{current}/{size}")
    public CommonResult getQualityByCondition(@PathVariable("current") int current,
                                              @PathVariable("size") int size,
                                              @RequestBody QualityConditionDto condition){
        SelectResult selectResult = qualityService.getByCondition(current, size, condition);
        if (selectResult.getTotal() == 0){
            return CommonResult.success("未查询到结果");
        }else{
            return CommonResult.success(selectResult);
        }
    }

    /**
     * 单独的数据通过id
     * 根据id搜索
     *
     * @param qualityData 质量数据
     * @return {@link CommonResult}
     */
    @RequestMapping("/getSingleDataById")
    public CommonResult getSingleDataById(@RequestBody QualityDataInfo qualityData){
        if (qualityData.getId() <= 0){
            return CommonResult.error(CommonResultEm.ERROR,"请输入正确质控数据id");
        }
        QualityDataInfo qualityDataInfo = qualityService.getById(qualityData.getId());
        if (qualityDataInfo == null){
            return CommonResult.error(CommonResultEm.ERROR,"请输入正确质控数据Id");
        }else{
            return CommonResult.success(qualityDataInfo);
        }
    }
}
