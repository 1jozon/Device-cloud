package cn.rmy.mqttUtils;

import cn.rmy.common.beans.MessageType;
import cn.rmy.common.jsonUtils.JsonUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqttSendHandle {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    MessageType messageType = new MessageType();

    public void sendHandle(String topic, int qos, String payload){
        messageType.setTopic(topic);
        messageType.setQos(qos);
        messageType.setPayload(payload);
        rabbitTemplate.convertAndSend("rmyDirectExchange","rmy.queue.send", JsonUtil.jsonToString(messageType));


    }
    public void sendHandle(String topic, String payload){
        messageType.setTopic(topic);
        messageType.setQos(2);
        messageType.setPayload(payload);
        rabbitTemplate.convertAndSend("rmyDirectExchange","rmy.queue.send", JSONObject.toJSONString(messageType));
    }

}
