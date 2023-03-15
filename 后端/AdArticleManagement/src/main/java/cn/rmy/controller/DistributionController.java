package cn.rmy.controller;

import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.pojo.dto.City;
import cn.rmy.common.pojo.dto.Province;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.service.imp.DistributeServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 仪器分布控制器
 *
 * @author chu
 * @date 2021/12/01
 */
@RestController
@RequestMapping("/rmy/distributeMaps")
public class DistributionController {

    @Autowired
    private DistributeServiceImp distributeService;

    @RequestMapping("/provinces")
    public CommonResult provinceDistribute(){
        List<Province> provinceList = distributeService.getProvinceDistribute();

        if (provinceList == null || provinceList.size() == 0){
            return CommonResult.error(CommonResultEm.ERROR,"请求失败！");
        }
        return CommonResult.success(provinceList);


    }

    @RequestMapping("/citys")
    public CommonResult cityDistribute(@RequestParam String province){

        List<City> cityList = distributeService.getCityDistribute(province);

        if (cityList == null || cityList.size() == 0){
            return CommonResult.error(CommonResultEm.ERROR,"未找到"+province+"仪器分布信息！");
        }
        return CommonResult.success(cityList);
    }

}
