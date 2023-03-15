package cn.rmy.config;

import cn.rmy.redisUtils.RedisUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
//public class RedisSessionDAO extends CachingSessionDAO {
public class RedisSessionDAO extends AbstractSessionDAO {
    //redis 存入前缀
    private static final String PREFIX = "SHIRO_SESSION_ID";

    //有限期
    private static final int EXPRIE = 24 * 60 * 60;

    @Autowired
    private RedisUtils redisUtils;


    /**
     * session创建session时，将session存入redis
     *
     * @param session 会话
     * @return {@link Serializable}
     */
    @Override
    protected Serializable doCreate(Session session) {
        //生成sessionID
        Serializable serializable = this.generateSessionId(session);
        this.assignSessionId(session,serializable);
        //将sessionID作为kry，session作为value存入redis
        //redisTemplate.opsForValue().set(serializable,session);

       // redisUtils.set((String) session.getId(), session);
        redisUtils.setSerialiable(session.getId(), session);
        return serializable;
    }

    /**
     * 当用户维持会话时，刷新session的有效时间
     *
     * @param session 会话
     */
/*    @Override
    protected void doUpdate(Session session) {
        //设置session有效期
        session.setTimeout(EXPRIE * 1000);
        //redisTemplate.opsForValue().set(session.getId(), session, EXPRIE, TimeUnit.SECONDS);
        redisUtils.updateSeriable(session.getId(),session,EXPRIE,TimeUnit.SECONDS);
    }*/

    /**
     * shiro通过sessionId获取Session对象，从redis中获取
     *
     * @param sessionId 会话id
     * @return {@link Session}
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        //System.out.println(1);
        if(sessionId == null){
            return null;
        }
        //Session session = (Session) redisUtils.getSerialable(sessionId);
        //return session;

        //解决频繁请求问题
        HttpServletRequest request = getRequest();
        if(request != null){
            Session sessionObj = (Session) request.getAttribute(sessionId.toString());
            if(sessionObj != null){
                return sessionObj;
            }
        }
        Session session = (Session) redisUtils.getSerialable(sessionId);
        if(session != null && request != null){
            request.setAttribute(session.getId().toString(), session);
        }
        return session;
    }


    /**
     * 当用户注销或会话过期时,将session从redis删除
     *
     * @param session 会话
     */
/*    @Override
    protected void doDelete(Session session) {
        if(session == null){
            return;
        }
        //redisTemplate.delete(session.getId());
        redisUtils.deleteSerial(session.getId());

        //解决频繁请求问题
        HttpServletRequest request = getRequest();
        if (request != null){
            request.removeAttribute(session.getId().toString());
        }
    }*/


    @Override
    public void update(Session session) throws UnknownSessionException {
        session.setTimeout(EXPRIE * 1000);
        //redisTemplate.opsForValue().set(session.getId(), session, EXPRIE, TimeUnit.SECONDS);
        redisUtils.updateSeriable(session.getId(),session,EXPRIE,TimeUnit.SECONDS);
    }

    @Override
    public void delete(Session session) {
        if(session == null){
            return;
        }
        //redisTemplate.delete(session.getId());
        redisUtils.deleteSerial(session.getId());

        //解决频繁请求问题
        HttpServletRequest request = getRequest();
        if (request != null){
            request.removeAttribute(session.getId().toString());
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return null;
    }

    private HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes != null ? requestAttributes.getRequest() : null;
    }
}
