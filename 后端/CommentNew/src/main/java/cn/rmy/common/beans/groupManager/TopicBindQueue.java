package cn.rmy.common.beans.groupManager;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "rabbit")
public class TopicBindQueue {
    private Map<String,String> topicbindqueue;
}
