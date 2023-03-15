package cn.rmy.rabbitUtils;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;


@Configuration
public class MqttDataRabbitConfig {

    //队列取名
    //final static  String queueName = "rmy.queue.first,rmy.queue.send";

    @Value("${rabbit.queues}")
    private String queueName;

    @Value("${rabbit.exchange}")
    private String exchange;

    //创建队列
    @Bean
    public Queue getQueueA(){
        String[] queueNames = queueName.trim().split(",");
        return new Queue(queueNames[0],true);
    }
    //创建队列
    @Bean
    public Queue getQueueB(){
        String[] queueNames = queueName.trim().split(",");
        return new Queue(queueNames[1],true);
    }

    //创建队列
    @Bean
    public Queue getQueueConnect(){
        String[] queueNames = queueName.trim().split(",");
        return new Queue(queueNames[2],true);
    }

    @Bean
    public Queue getQueueUpgrade(){
        String[] queueNames = queueName.trim().split(",");
        return new Queue(queueNames[3],true);
    }

    @Bean
    public Queue getQueueCheckData(){
        String[] queueNames = queueName.trim().split(",");
        return new Queue(queueNames[4],true);
    }

    @Bean
    public Queue getQueueReagentSurplus(){
        String[] queueNames = queueName.trim().split(",");
        return new Queue(queueNames[5],true);
    }

    @Bean
    public Queue getQueueReagentMainCurve(){
        String[] queueNames = queueName.trim().split(",");
        return new Queue(queueNames[6],true);
    }

    @Bean
    public Queue getQueueCaliData(){
        String[] queueNames = queueName.trim().split(",");
        return new Queue(queueNames[7],true);
    }

    @Bean
    public Queue getQueueConsumables(){
        String[] queueNames = queueName.trim().split(",");
        return new Queue(queueNames[8],true);
    }

    @Bean
    public Queue getQueueFault(){
        String[] queueNames = queueName.trim().split(",");
        return new Queue(queueNames[9],true);
    }

    @Bean
    public Queue getQueueQualityData(){
        String[] queueNames = queueName.trim().split(",");
        return new Queue(queueNames[10],true);
    }

    @Bean
    public Queue getQueueGPSInfo(){
        String[] queueNames = queueName.trim().split(",");
        return new Queue(queueNames[11],true);
    }

    @Bean
    public Queue getQueueProjectInfo(){
        String[] queueNames = queueName.trim().split(",");
        return new Queue(queueNames[12],true);
    }



    //direct交换机 起名rmyDirectExchange
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange("rmyDirectExchange",true,false);
    }




    //绑定  将队列和交换机绑定, 并设置用于匹配键：directExchange
    @Bean
    Binding bindingYihonWqmMessageA() {
       return BindingBuilder.bind(getQueueA()).to(directExchange()).with(getQueueA().getActualName());
    }
    @Bean
    Binding bindingYihonWqmMessageB() {
        return BindingBuilder.bind(getQueueB()).to(directExchange()).with(getQueueB().getActualName());
    }

    @Bean
    Binding bindingYihonWqmMessageConnect() {
        return BindingBuilder.bind(getQueueConnect()).to(directExchange()).with(getQueueConnect().getActualName());
    }

    @Bean
    Binding bindingYihonWqmMessageUpgrade() {
        return BindingBuilder.bind(getQueueUpgrade()).to(directExchange()).with(getQueueUpgrade().getActualName());
    }

    @Bean
    Binding bindingYihonWqmMessageCheckData() {
        return BindingBuilder.bind(getQueueCheckData()).to(directExchange()).with(getQueueCheckData().getActualName());
    }

    @Bean
    Binding bindingYihonWqmMessageReagentSurplus() {
        return BindingBuilder.bind(getQueueReagentSurplus()).to(directExchange()).with(getQueueReagentSurplus().getActualName());
    }

    @Bean
    Binding bindingYihonWqmMessageReagentMainCurve() {
        return BindingBuilder.bind(getQueueReagentMainCurve()).to(directExchange()).with(getQueueReagentMainCurve().getActualName());
    }

    @Bean
    Binding bindingYihonWqmMessageCaliData() {
        return BindingBuilder.bind(getQueueCaliData()).to(directExchange()).with(getQueueCaliData().getActualName());
    }

    @Bean
    Binding bindingYihonWqmMessageConsumables() {
        return BindingBuilder.bind(getQueueConsumables()).to(directExchange()).with(getQueueConsumables().getActualName());
    }

    @Bean
    Binding bindingYihonWqmMessageFault() {
        return BindingBuilder.bind(getQueueFault()).to(directExchange()).with(getQueueFault().getActualName());
    }

    @Bean
    Binding bindingYihonWqmMessageQualityData() {
        return BindingBuilder.bind(getQueueQualityData()).to(directExchange()).with(getQueueQualityData().getActualName());
    }

    @Bean
    Binding bindingYihonWqmMessageGPSInfo() {
        return BindingBuilder.bind(getQueueGPSInfo()).to(directExchange()).with(getQueueGPSInfo().getActualName());
    }

    @Bean
    Binding bindingYihonWqmMessageProjectInfo() {
        return BindingBuilder.bind(getQueueProjectInfo()).to(directExchange()).with(getQueueProjectInfo().getActualName());
    }

}
