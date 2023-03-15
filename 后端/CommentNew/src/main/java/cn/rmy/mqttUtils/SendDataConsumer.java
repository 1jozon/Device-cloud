package cn.rmy.mqttUtils;


import cn.rmy.common.beans.MessageType;
import cn.rmy.common.jsonUtils.JsonUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "rmy.queue.send")
public class SendDataConsumer {

    @Autowired
    private MqttReceiveClient.MqttGateway mqttGateway;
/*
*  从消息队列取出待发送的消息
* */

    @RabbitHandler
    public void process(String message) throws InterruptedException {
        JSONObject object = JSONObject.parseObject(message);
        Thread.sleep(100);
        //System.out.println(message);
        log.info(message);
        mqttGateway.sendToMqtt((String) object.getJSONObject("data").get("topic"),
                (int) object.getJSONObject("data").get("qos"),
                (String) object.getJSONObject("data").get("payload"));
    }

}
