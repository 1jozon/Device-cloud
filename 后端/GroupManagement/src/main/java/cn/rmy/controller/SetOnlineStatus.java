package cn.rmy.controller;

import cn.rmy.service.InstrumentService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "rmy.queue.connect")
public class SetOnlineStatus {

    @Autowired
    private InstrumentService instrumentService;

    @RabbitHandler
    public void process(String message){
        JSONObject object = JSONObject.parseObject(message);
        String instrumentId = (String) object.get("clientid");
        int status = 0;
        if(object.containsKey("connected_at")){
            status=1;
        }
        else if(object.containsKey("disconnected_at")){
            status=-1;
        }
        int rec = instrumentService.setOnlineStatus(instrumentId,status);
        if(rec>0) log.info("instrumentId:{},{}",instrumentId,status==-1?"已离线":"上线啦");
    }
}
