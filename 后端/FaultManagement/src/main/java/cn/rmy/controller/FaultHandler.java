package cn.rmy.controller;

import cn.rmy.common.beans.faultManagement.FaultRecordReq;
import cn.rmy.service.FaultRecordService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Slf4j
@Component
//@RabbitListener(queues = "rmy.queue.$share/g1/fault")
@RabbitListener(queues = "rmy.queue.$share/g2/fault")
public class FaultHandler {

    private final static Logger logger = LoggerFactory.getLogger(FaultRecordController.class);

    @Autowired
    private FaultRecordService faultRecordService;


    @RabbitHandler
    public void process(String message) throws ParseException {
        FaultRecordReq faultRecordReq = new FaultRecordReq();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject obj = JSONObject.parseObject(message);
        //System.out.println(obj.getString("faultTime"));
        faultRecordReq.setDeviceId(obj.get("ID").toString())
                .setFaultClass(obj.get("faultClass").toString())
                .setFaultCode(obj.get("faultCode").toString())
                .setModuleCode(obj.get("moduleCode").toString());

        faultRecordReq.setFaultTime(ft.parse(obj.get("faultTime").toString()));

        logger.info("信息：{}",faultRecordReq);
        faultRecordService.insert(faultRecordReq);
    }
}
