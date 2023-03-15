package cn.rmy.common.config;

import com.baomidou.mybatisplus.extension.api.R;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.apache.shiro.session.Session;
import org.omg.CORBA.portable.UnknownException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * redis配置序列化
 *
 * @author chu
 * @date 2022/03/21
 */
@Configuration
@EnableCaching
public class RedisConfig {
    /*@Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.pool.max-active}")
    private int maxActive;
    @Value("${spring.redis.pool.max-wait}")
    private int maxWait;
    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;*/

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) throws UnknownException{

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        //json序列化配置
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        //制定序列化的域，filed、get和set，以及修饰符的范围，ANY是都有，包括private、public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String、Integer等会抛出异常
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(om);

        //配置具体的序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        //key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);

        //hash的key也采用String的序列化方式
        template.setHashKeySerializer(jackson2JsonRedisSerializer);

       //value序列化采用json
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    /**
     * session
     *
     * @param factory 工厂
     * @return {@link RedisTemplate}<{@link Serializable}, {@link Session}>
     * @throws UnknownException 未知的异常
     */
    @Bean
    RedisTemplate<Serializable, Session> sessionRedisTemplate(RedisConnectionFactory factory) throws UnknownException{
        RedisTemplate<Serializable, Session> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        //json序列化配置
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        //制定序列化的域，filed、get和set，以及修饰符的范围，ANY是都有，包括private、public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String、Integer等会抛出异常
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(om);

        //配置具体的序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        //redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        //不配置value
        return redisTemplate;
    }

}
