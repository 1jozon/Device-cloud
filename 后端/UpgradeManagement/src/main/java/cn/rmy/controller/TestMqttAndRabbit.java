package cn.rmy.controller;


import cn.rmy.mqttUtils.MqttReceiveClient;
import cn.rmy.mqttUtils.MqttSendHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/testRabbit")
public class TestMqttAndRabbit {

    @Autowired
    private MqttSendHandle mqttSendHandle;

    @RequestMapping("/sendMessage/{n}")
    public String sendMessage(@PathVariable int n){
        for(int i=0;i<n;i++){
            mqttSendHandle.sendHandle("/tjk",2,"发送mqtt消息_"+i);
        }
        return "发布消息"+n+"条";
    }

}
