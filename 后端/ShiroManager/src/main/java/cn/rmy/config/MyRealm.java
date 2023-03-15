package cn.rmy.config;

import cn.rmy.common.dto.Role;
import cn.rmy.common.dto.Users;
import cn.rmy.service.imp.UsersServiceImp;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;


public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UsersServiceImp userService;

    private Logger logger = LoggerFactory.getLogger(MyRealm.class);

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
     //   logger.info("进行授权");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Users users = (Users) principals.getPrimaryPrincipal();
        Set<Role> roleSet = users.getRoles();
        for (Role role : roleSet){
            if (role.getRoleId() == users.getCurrentRoleId()){
                simpleAuthorizationInfo.addRole(role.getRoleName());
                if (role.getPermissions() != null){
                    simpleAuthorizationInfo.addStringPermissions(role.getPermissions());
                }
            }
        }

        return simpleAuthorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
   //     logger.info("进行认证");

        String name =token.getPrincipal().toString();
        Users users = userService.getUserById(name);
        if (users == null){
            logger.info("用户不存在");
            return null;
        }

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(users, users.getUserPassword(),getName());

        return simpleAuthenticationInfo;
    }
}
