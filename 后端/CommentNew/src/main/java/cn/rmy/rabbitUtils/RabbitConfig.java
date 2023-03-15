package cn.rmy.rabbitUtils;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
public class RabbitConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;



    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host,port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        //设置发布验证
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.SIMPLE);
        return connectionFactory;

    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }


    /*
    @Bean
    public RabbitConfig config() throws IOException, TimeoutException {
        Connection conn = (Connection) connectionFactory().createConnection();
        Channel channel = conn.createChannel();

        channel.exchangeDeclare("direct",exchange);
        String[] queueNames = queueName.trim().split(",");
        for(int i=0;i<queueNames.length;i++){
            channel.queueDeclare(queueNames[i],false,false,false,null);
            channel.queueBind(queueNames[i],exchange,queueNames[i]);
        }
        channel.close();
        conn.close();
        return null;
    }
*/

}













