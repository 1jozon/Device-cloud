package cn.rmy.mqttUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "rmy.queue.first")
public class MqttDataRabbitConsumer {

    @RabbitHandler
    public void process(String message) throws InterruptedException {
        log.info("消费者收到消息："+message);
        Thread.sleep(50);
    }
}
