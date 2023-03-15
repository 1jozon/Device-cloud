package cn.rmy.mqttUtils;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Configuration
@IntegrationComponentScan
public class MqttReceiveClient {

    @Autowired
    private MqttReceiveHandle mqttReceiveHandle;

    @Value("${mqtt.address}")
    private String mqttAddress;//mqtt服务器的地址和端口号

    @Value("${mqtt.clientId}")
    private String clientId;

    @Value("${mqtt.userName}")
    private String userName;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.defaultTopic}")
    private String defaultTopic;

    @Value("${mqtt.connectTimeOut}")
    private int connectTimeOut;

    @Bean(value = "getMqttConnectOptions")
    public MqttConnectOptions getMqttConnectOptions(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(userName);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{mqttAddress});
        //mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setConnectionTimeout(5);
        //设置会话心跳时间
        mqttConnectOptions.setKeepAliveInterval(5);
        mqttConnectOptions.setAutomaticReconnect(true);
        return mqttConnectOptions;
    }

    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory(){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());

        return factory;
    }

    //接收通道
    @Bean
    public MessageChannel mqttInputChannel(){
        return new DirectChannel();
    }

    //配置client，监听topic
    @Bean
    public MessageProducer inbound(){
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId + "_inbound2", mqttPahoClientFactory(),defaultTopic.trim().split(","));
        adapter.setCompletionTimeout(connectTimeOut);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler(){
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                mqttReceiveHandle.handle(message);
            }
        };
    }

//-------------------mqtt消息发送-----------------------
    @Bean
    public MessageChannel mqttOutboundChannel(){
        return new DirectChannel();
    }

    /*
    * 发送消息和接收消息Channel可以使用相同的MqttPahoClientFactory
    * */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler outbound(){
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId+"_outbound2",mqttPahoClientFactory());
        messageHandler.setAsync(true);//如果设置成true，发送消息时将不会阻塞。
        messageHandler.setDefaultTopic("testTopic");
        //messageHandler.setCompletionTimeout();
        return messageHandler;
    }

    //defaultRequestChannel指定发送消息绑定的channel
    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    @Component
    public interface MqttGateway{
        //定义重载方法，用于消息发送
        void sendToMqtt(String payload);
        void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String payload);
        void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);
    }
















}
