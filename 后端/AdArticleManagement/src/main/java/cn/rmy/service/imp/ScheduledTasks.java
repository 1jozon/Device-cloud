package cn.rmy.service.imp;

import cn.rmy.common.beans.articleGps.ArticleInfo;
import cn.rmy.common.pojo.dto.CaliMsgDTO;
import cn.rmy.redisUtils.RedisUtils;
import cn.rmy.dao.ArticleDao;
import cn.rmy.service.Imp.CaliPointDataServiceImp;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 日程安排的任务
 *
 * @author chu
 * @date 2021/11/22
 */
@Component
@Slf4j
public class ScheduledTasks {

    private Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ArticleServiceImp articleService;

    @Autowired
    private CaliPointDataServiceImp caliPointDataService;

    /**
     * 同步文章访问量
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void syncPostViews(){
        //logger.info("同步文章访问量");
        String keyPattern = "article:*";
        Set<String> keySet = stringRedisTemplate.keys(keyPattern);
        //System.out.println(keySet.size());
        for (String key : keySet){
            Object object = stringRedisTemplate.opsForValue().get(key);
            if (object == null){
                continue;
            }
            Integer count = Integer.parseInt(object.toString());

            String idString = key.substring(8);
            int id = Integer.parseInt(idString);
            QueryWrapper<ArticleInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",id);
            ArticleInfo articleInfo = articleDao.selectOne(queryWrapper);
            if (articleInfo == null ){
                stringRedisTemplate.opsForValue().getOperations().delete(key);
                continue;
            }
            count += articleInfo.getCountRead();
            articleInfo.setCountRead(count);
            int rec = articleDao.updateById(articleInfo);
            stringRedisTemplate.opsForValue().getOperations().delete(key);
        }
    }

    /**
     * 定时更新异常定标数据到数据库
     * 从0点开始，每2小时执行一次
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
    //@Scheduled(cron = "0 0/2 * * * ?")
    public void insertCaliDataInCeche(){
        logger.info("定标异常数据处理");
        Set<String> keysList = new HashSet<>();
        boolean keys = false;
        List<CaliMsgDTO> caliMsgList = new ArrayList<>();
        try {
            //keys = redisTemplate.hasKey("cali" + "*");
            keysList = redisTemplate.keys("cali:" + "*");
            if(keysList == null || keysList.size() == 0){
                logger.info("定标异常数据为空");
                return;
            }
            logger.info("定标异常数据不为空");
            caliMsgList = redisTemplate.opsForValue().multiGet(keysList);

            for(CaliMsgDTO info : caliMsgList)
            {
                caliPointDataService.insertNewData(info);
                redisTemplate.delete("cali:" + info.getCaliId());
            }
        }catch (Exception e){
            System.out.println("定任务-定标出现异常!");
            e.getCause();
        }
    }

    /**
     * 定时发送推送过的文章
     * 每天 6点
     */
    @Scheduled(cron = "0 0 5 * * ?")
   // @Scheduled(cron = "0/30 * * * * ?")
    public void realeaseArticle(){
        logger.info("定时文章");
        try {
            articleService.pushAllAdOnScheduled();
        }catch (Exception e){
            System.out.println("定时任务-文章出现故障");
            e.printStackTrace();
        }
    }


/*    *//**
     * 测试计划 5a
     *//*
    @Scheduled(fixedDelay = 5000)
    public void testScheduled(){
        Thread t = Thread.currentThread();
        logger.info("定时测试1");
        logger.info("taskSchedule1 " + new Date() + t.getId());
    }*/
}
