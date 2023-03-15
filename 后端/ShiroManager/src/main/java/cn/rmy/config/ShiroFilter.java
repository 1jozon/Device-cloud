package cn.rmy.config;

import cn.rmy.common.dto.Users;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ShiroFilter extends FormAuthenticationFilter {

    protected static final String SINGNATURE_TOKEN = "加密token";

    //表示是否允许访问
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        //return super.isAccessAllowed(request, response, mappedValue);
        return false;
    }

    //表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可。
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        String token = getRequestToken((HttpServletRequest) request);
        String login = ((HttpServletRequest) request).getServletPath();

        //登录允许通过
        if ("/user/login".equals(login)){
            return true;
        }
        if (StringUtils.isBlank(token)){
            System.out.println("没有token");
            return false;
        }

        //从当前用户获取信息
        Users users = (Users) SecurityUtils.getSubject().getPrincipal();
        if (users == null){
            return false;
        }
        //对当前ID进行sha256加密
        String encryptionKey = DigestUtils.sha256Hex(SINGNATURE_TOKEN + users.getUserId());
        if (encryptionKey.equals(token)){
            return true;
        }else{
            System.out.println("无效token");
        }
        return false;
    }


    private String getRequestToken(HttpServletRequest request){

        //默认从请求头中获取token
        String token = request.getHeader("token");

        //如果头header中没有token，则从参数中获取token
        if (StringUtils.isBlank(token)){
            token = request.getParameter("token");
        }
        return token;
    }
}
