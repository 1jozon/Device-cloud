package cn.rmy.mqttUtils;

import cn.rmy.common.beans.groupManager.TopicBindQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Component
public class MqttReceiveHandle {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbit.queues}")
    private String queueName;

    @Autowired
    private TopicBindQueue topicBindQueue;

    private Map<String,String> map;
//收到mqtt信息在这处理，决定发到哪个队列
    public void handle(Message<?> message){
        map = topicBindQueue.getTopicbindqueue();
        log.info("主题：{}", message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));
        //System.out.println(message);
        //System.out.println(map);
        String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        String[] queueNames = queueName.split(",");
        if(topic.endsWith("connected")||topic.endsWith("disconnected")){
            rabbitTemplate.convertAndSend("rmyDirectExchange",queueNames[2],(String)message.getPayload());
        }
        else{
            rabbitTemplate.convertAndSend("rmyDirectExchange",map.get(topic),(String)message.getPayload());
        }
        //发送消息到rabbit消息队列
        //System.out.println(message.getHeaders());
        //rabbitTemplate.convertAndSend("rmyDirectExchange",queueNames[0],(String)message.getPayload());
    }
}
