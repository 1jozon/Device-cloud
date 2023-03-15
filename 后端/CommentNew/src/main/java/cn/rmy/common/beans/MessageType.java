package cn.rmy.common.beans;

import lombok.Data;

@Data
public class MessageType {
    public String topic;
    public int qos;
    public String payload;
}
