package cn.rmy.mqttUtils;


import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class MQTTConnect {
    @Value("${mqtt.address}")
    private String mqttAddress;//mqtt服务器的地址和端口号

    @Value("${mqtt.clientId}")
    private String clientId;

    @Value("${mqtt.userName}")
    private String userName;

    @Value("${mqtt.password}")
    private String password;

    @Value("${filePath}")
    public String filePath;

    private MqttClient mqttClient;

    /**
     *
     *
     * @param mqttCallback 回调函数
     * @throws MqttException
     */
    public void setMqttClient(MqttCallback mqttCallback) throws MqttException{
        MqttConnectOptions options = mqttConnectOptions();
        if(mqttCallback==null){
            log.info("未设置MQTT回调函数CallBack(),无法完成连接");
        }
        else {
            mqttClient.setCallback(mqttCallback);
        }
        mqttClient.connect(options);
    }


    /**
     * MQTT连接参数设置
     * @return
     */
    public MqttConnectOptions mqttConnectOptions() throws MqttException{
        mqttClient = new MqttClient(mqttAddress,clientId,new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(userName);
        options.setPassword(password.toCharArray());
        options.setConnectionTimeout(10);
        options.setAutomaticReconnect(true);
        options.setCleanSession(false);


        return options;
    }

    /**
     * 关闭mqtt连接
     * @throws MqttException
     */
    public void close() throws  MqttException{
        mqttClient.close();
        mqttClient.disconnect();
    }

    /**
     * 向某个主题发布消息 默认Qos为1
     * @param topic
     * @param msg
     * @throws MqttException
     */
    public void publish(String topic, String msg) throws MqttException{
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(msg.getBytes());
        MqttTopic mqttTopic = mqttClient.getTopic(topic);
        MqttDeliveryToken token = mqttTopic.publish(mqttMessage);
        token.waitForCompletion();
    }

    /**
     * 向某个主题发布消息
     * @param topic
     * @param msg
     * @throws MqttException
     */
    public void publish(String topic, String msg, int qos) throws MqttException{
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(msg.getBytes());
        mqttMessage.setQos(qos);
        MqttTopic mqttTopic = mqttClient.getTopic(topic);
        MqttDeliveryToken token = mqttTopic.publish(mqttMessage);
        token.waitForCompletion();
    }

    /**
     * 订阅某个主题 默认qos为1
     * @param topic
     * @throws MqttException
     */
    public void subscribe(String topic) throws MqttException{
        mqttClient.subscribe(topic);
    }

    /**
     * 订阅某个主题
     * @param topic
     * @throws MqttException
     */
    public void subscribe(String topic, int qos) throws MqttException{
        mqttClient.subscribe(topic,qos);
    }


}
