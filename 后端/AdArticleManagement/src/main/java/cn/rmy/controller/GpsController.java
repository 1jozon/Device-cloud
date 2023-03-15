package cn.rmy.controller;

import cn.rmy.common.beans.articleGps.GpsInfo;
import cn.rmy.common.dto.Users;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.service.GpsService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GPS控制器
 *
 * @author chu
 * @date 2021/11/12
 */
@RestController
@RequestMapping("rmy/gps")
public class GpsController {

    @Autowired
    private GpsService gpsService;

    /**
     * 插入ins地址信息
     *
     * @param gpsinfo gpsinfo
     * @return {@link CommonResult}
     */
    @RequestMapping("insertInstAddress")
    public CommonResult insertInsAddress(@RequestBody GpsInfo gpsinfo){
        if (gpsinfo == null){
            return CommonResult.error(CommonResultEm.ERROR,"请输入正确仪器lac、cid信息!");
        }
        int rec = gpsService.intoSql(gpsinfo);
        switch (rec){
            case -1: return CommonResult.error(CommonResultEm.ERROR,"添加失败，添加数据库失败！");

            case 0:  return CommonResult.error(CommonResultEm.ERROR,"添加失败，仪器信息异常，未找到该仪器！");

            case 1:  return CommonResult.success("添加成功！");

            case 2: return CommonResult.error(CommonResultEm.ERROR,"添加失败，仪器已有地址信息！");

            default: return CommonResult.error(CommonResultEm.ERROR,"添加失败，未知错误！");
        }
    }


    /**
     * 串货通知
     *
     * @param gpsInfo gps信息
     * @return {@link CommonResult}
     */
    @RequestMapping("/colNotice")
    public CommonResult collusionNotice(@RequestBody GpsInfo gpsInfo){

        //获取用户
        Subject currentUser = SecurityUtils.getSubject();
        Users user = (Users) currentUser.getPrincipal();
        String currentUserId = user.getUserId();


        if (gpsInfo == null){
            return CommonResult.error(CommonResultEm.ERROR,"失败，地址信息为空！");
        }
        //串货判断
        GpsInfo oldGpsInfo = gpsService.collusion(gpsInfo);
        if (oldGpsInfo == null){
            return CommonResult.success("未串货！");
        }else {
            //串货发送通知
            int rec = gpsService.collusionNotice(gpsInfo,oldGpsInfo,currentUserId);
            if (rec == -1){
                return CommonResult.error(CommonResultEm.ERROR,"发生串货，出现通知异常！");
            }else {
                return CommonResult.success("出现串货，已发送邮件！");
            }
        }
    }

}
