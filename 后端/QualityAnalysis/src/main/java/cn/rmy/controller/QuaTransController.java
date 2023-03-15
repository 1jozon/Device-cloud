package cn.rmy.controller;

import cn.rmy.common.pojo.dto.QualityDataDto;
import cn.rmy.service.imp.QualityServiceImp;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 消息传输
 *
 * @author chu
 * @date 2021/12/28
 */
@Slf4j
@Component
//@RabbitListener(queues = "rmy.queue.$share/g1/qualityData")
@RabbitListener(queues = "rmy.queue.$share/g2/qualityData")
public class QuaTransController {

    @Autowired
    private QualityServiceImp qualityService;

    @RabbitHandler
    @Async("threadPoolTaskExecutor")
    public void quaProcess(String message) throws Exception{
        //System.out.println("质控数据接收线程："+Thread.currentThread().getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        JSONObject object = JSONObject.parseObject(message);
        QualityDataDto qualityData = new QualityDataDto();
        qualityData.setInsId((String) object.get("ID"));
        qualityData.setProjectId((String) object.get("ProjectID"));
        qualityData.setProjectName((String) object.get("projectName"));
        qualityData.setQctrlRlu((String) object.get("qctrlRLU"));
        qualityData.setQctrlResult((String) object.get("qctrlResult"));
        qualityData.setQctrlId((String) object.get("qctrlID"));
        qualityData.setReagentBatchId((String) object.get("reagentBatchID"));
        int exception = 0;
        String excep = (String)object.get("exception");
        if(excep != null && (!excep.equals(""))&& (!excep.equals("0"))){
            exception = 1;
        }
        System.out.println(excep);
        System.out.println(exception);

        qualityData.setException(exception);
        qualityData.setQctrlTime(format.parse((String) object.get("qctrlTime")));

/*        int rec = qualityService.insertQualityInfo(qualityData);
        if (rec != 1){
            log.info("添加质控数据失败");
        }else {
            log.info("添加质控数据成功");
        }*/

        //线程池：异步
        //Long start = System.currentTimeMillis();
        qualityService.AsyncInsertQualityInfo(qualityData);
      //  System.out.println(String.format("耗时{%s}", System.currentTimeMillis() - start));
    }
}
