package cn.rmy.controller;

import cn.rmy.common.pojo.dto.InsMsgDto;
import cn.rmy.common.beans.articleGps.GpsInfo;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.dao.GpsMapper;
import cn.rmy.service.imp.InsMsgServiceImp;
import cn.rmy.service.imp.LabelServiceImp;
import cn.rmy.service.imp.PictureServiceImp;
import cn.rmy.service.imp.ScheduledTasks;
import com.aliyun.oss.model.OSSObjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 测试控制器
 *
 * @author chu
 * @date 2021/11/09
 */
@RestController
@RequestMapping(value = "rmy/test")
public class TestController {

    @Autowired
    private PictureServiceImp pictureService;

    @Autowired
    private GpsMapper gpsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ScheduledTasks scheduledTasks;

    @Autowired
    private LabelServiceImp labelService;

    @Autowired
    private InsMsgServiceImp insMsgService;

    @RequestMapping(value = "/testCosUrl")
    public CommonResult tesUrl(@RequestParam("file") MultipartFile file, Integer type){

        String url = pictureService.uploadPictureFile(file,type);

        return CommonResult.success(url);
    }

    @RequestMapping(value = "/testDelete")
    public CommonResult deleteFile(@RequestParam String url){

        boolean flag = pictureService.deleteFile(url);

        return CommonResult.success(flag);
    }

    /**
     *
     *
     * @return {@link CommonResult}
     */
    @RequestMapping("/testGetFileList")
    public CommonResult getUpAvatarFile(){
        List<OSSObjectSummary> list = pictureService.getOssPicFile(3);
        return CommonResult.success(list);
    }

    /**
     * 测试gpsMapper
     *
     * @return {@link CommonResult}
     */
    @RequestMapping("/testMapper")
    public CommonResult testMapper(){
        List<GpsInfo> gpsInfos = gpsMapper.getNewestInsAddresses("1s");
        return CommonResult.success(gpsInfos);
    }


    // 测试记录阅读量
    @RequestMapping("/testRedis")
    public CommonResult testRedisCount(String id){
        Boolean a = redisTemplate.hasKey(id);
        Long count ;
        if (a){

            Object object = redisTemplate.opsForValue().get(id);
            count = Long.parseLong(object.toString());
            System.out.println(count);

            count = stringRedisTemplate.opsForValue().increment(id);
            System.out.println(count);
        }
        return CommonResult.success("chenggong");
    }

    //测试定时发送
    @RequestMapping("/testSheduled")
    public CommonResult testSheduled(){
        scheduledTasks.syncPostViews();
        return CommonResult.success();
    }

    //测试mp——insert后自动填充返回实体
    @RequestMapping("/insertBackId")
    public CommonResult testMpInsert(){
        int id = labelService.insertBackId();
        return CommonResult.success(id);
    }

    /**
     * 得到gps信息
     *
     * @param insId ins id
     * @return {@link CommonResult}
     */
    @RequestMapping("/getGpsMsg")
    public CommonResult getGpsMsg(String insId){
        InsMsgDto insMsg = insMsgService.getMsgByInsId(insId);
        if (insMsg == null){
            return CommonResult.error(CommonResultEm.ERROR,"未找到信息");
        }
        return CommonResult.success(insMsg);
    }

    /**
     * 添加标签多条测试
     *
     * @param labelString 标签的字符串
     * @param labelFormat 标签格式
     * @return {@link CommonResult}
     */
    @RequestMapping("/addArticleLabelTest")
    public CommonResult addArticleLabelTest(String labelString, String[] labelFormat){
        System.out.println(labelString);
        System.out.println(labelFormat);

        return CommonResult.success("成功");
    }
 }
