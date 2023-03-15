package cn.rmy.redisUtils;

import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.extension.api.R;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 复述,跑龙套
 *
 * @author chu
 * @date 2022/03/22
 */
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;

   @Autowired
    private RedisTemplate<Serializable, Session> sessionRedisTemplate;

   /**
         * 普通存入
         * @param key
         * @param value
         * @return
         */
   public boolean set(String key,Object value){
            try {
                redisTemplate.opsForValue().set(key,value);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        /**
         * 普通获取key
         * @param key
         * @return
         */
        public Object get(String key){
            return key == null ? null : redisTemplate.opsForValue().get(key);
        }

        /**
         * 存入key，设置过期时长
         * @param key
         * @param value
         * @param time
         * @return
         */
        public boolean set(String key,Object value,long time){
            try {
                if(time > 0){
                    redisTemplate.opsForValue().set(key,value,time, TimeUnit.SECONDS);
                }else{
                    redisTemplate.opsForValue().set(key,value);
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        /**
         * 判断key是否存在
         * @param key
         * @return
         */
        public boolean exists(String key){
            try {
                return redisTemplate.hasKey(key);
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        /**
         * 删除key
         * @param key
         */
        public void del(String key){
            try {
                if(key != null && key.length() > 0){
                    redisTemplate.delete(key);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        /**
     * 写入缓存并指定库
     *
     * @param key      关键
     * @param value    价值
     * @param db       db
     * @param falgJson 赛场json
     * @param timeOut  时间
     * @return boolean
     */
        public boolean setOnDB(String key, Object value, int db, boolean falgJson, Long timeOut){
            boolean result = false;
            try{

                //LettuceConnectionFactory redisConnection = (LettuceConnectionFactory)redisTemplate.getConnectionFactory();
                RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
                DefaultStringRedisConnection stringRedisConnection = new DefaultStringRedisConnection(redisConnection);
                stringRedisConnection.select(db);
                if(falgJson){
                    stringRedisConnection.set(key, JSONUtils.toJSONString(value));
                }else{
                    stringRedisConnection.set(key, value.toString());
                }
                if(timeOut != null && timeOut != 0){
                    stringRedisConnection.expire(key, timeOut);
                }
                stringRedisConnection.close();
                result = true;
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }

        /**
     * 在db
     * 指定db查询缓存
     *
     * @param key 关键
     * @param db  db
     * @return {@link Object}
     */
        public Object getOnDB(String key, int db){
            Object res = null;
            try{
                RedisConnection redisConnection =  redisTemplate.getConnectionFactory().getConnection();
                DefaultStringRedisConnection stringRedisConnection = new DefaultStringRedisConnection(redisConnection);
                stringRedisConnection.select(db);
                res = stringRedisConnection.get(key);
                stringRedisConnection.close();

            }catch (Exception e){
                e.printStackTrace();
            }
            return res;
        }

        /**
     * 删除指定db
     *
     * @param key 关键
     * @param db  db
     */
        public void removeOnDB(String key, int db){
            try {
                RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
                DefaultStringRedisConnection stringRedisConnection = new DefaultStringRedisConnection(redisConnection);
                stringRedisConnection.select(db);
                if (stringRedisConnection.exists(key)) {
                    stringRedisConnection.del(key);
                }
                stringRedisConnection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    /**
     * 序列化session设置
     *
     * @param key     关键
     * @param session 会话
     * @return boolean
     */
        public boolean setSerialiable(Serializable key, Session session){
            try{
               // RedisTemplate<Serializable, Session> sessionRedisTemplate2 = new RedisTemplate<>();
                sessionRedisTemplate.opsForValue().set("session:" + key, session);
                //sessionRedisTemplate.opsForValue().set(key,session);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        public Object getSerialable(Serializable sessionId){
            try {
               // RedisTemplate<Serializable, Session> sessionRedisTemplate2 = new RedisTemplate<>();

                Object session = sessionRedisTemplate.opsForValue().get("session:" + sessionId);

                //Object session = sessionRedisTemplate.opsForValue().get(sessionId) == null ? null : sessionRedisTemplate.opsForValue().get(sessionId);
                return session;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }

    /**
     * 刷新到期时间
     *
     * @param sessionId 会话id
     * @param session   会话
     * @param expire    到期
     * @param timeUnit  时间单位
     */
        public void updateSeriable(Serializable sessionId, Session session, int expire, TimeUnit timeUnit){
            try{
         //       RedisTemplate<Serializable, Session> sessionRedisTemplate = new RedisTemplate<>();
                sessionRedisTemplate.opsForValue().set("session:" + sessionId, session, expire, timeUnit);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    /**
     * 删除
     *
     * @param sessionId 会话id
     */
    public void deleteSerial(Serializable sessionId){
            try{
                sessionRedisTemplate.delete("session:" + sessionId);
            }catch (Exception e){
                e.printStackTrace();
            }
        }



}
