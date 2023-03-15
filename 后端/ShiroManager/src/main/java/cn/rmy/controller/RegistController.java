package cn.rmy.controller;

import cn.rmy.common.beans.shiroUsers.UserInfo;
import cn.rmy.common.dto.LoginUsersDto;
import cn.rmy.common.dto.UpdateUser;
import cn.rmy.common.dto.Users;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.service.imp.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("rmy/entry")
public class RegistController {

    private Logger logger = LoggerFactory.getLogger(RegistController.class);
    private static String SINGNATURE_TOKEN = "加密token";

    @Autowired
    private UserInfoServiceImp userInfoService;

    @Autowired
    private UserRoleServiceImp userRoleService;

    @Autowired
    private RoleServiceImp roleService;

    @Autowired
    private SendEmailMsgServiceImp emailMsgService;

    @Autowired
    private UsersServiceImp userService;


    /**
     * 注册
     *
     * @param userInfo 用户信息
     * @return {@link CommonResult}
     */
    @RequestMapping("/register")
    public CommonResult register(@RequestBody UserInfo userInfo)throws Exception{

        if (userInfo == null){
            return CommonResult.error(CommonResultEm.NOT_EXIST,"用户为空");
        }
        userInfo.setRegistStatus(1);

        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        userInfo.setUserSalt(salt);

        int rec = userInfoService.insertUser(userInfo);
        int recRole = userRoleService.insert(userInfo.getUserId(),new int[]{userInfo.getRoleId()});
        logger.info("用户角色表插入状态（0：成功，-1：失败）" + recRole);

        if (rec == -1){
            return CommonResult.error(CommonResultEm.ERROR,"注册失败");
        }else if (rec == 1){
            return CommonResult.error(CommonResultEm.ALREADY_EXIST,"用户已存在");
        }else if (userInfo.getRoleId() == 0){
            return CommonResult.success("注册游客成功，请登录使用");
        }
        //发送注册成功、等待通知邮件
        try {
            emailMsgService.sendRegistEmailMsg(userInfo);
        }catch (Exception e){
            return CommonResult.error(CommonResultEm.ERROR,"注册失败，邮箱账号异常");
        }
        return CommonResult.success("注册成功，请耐心等待审核");
    }

    /**
     * 登录
     *
     * @param userId       用户id
     * @param userPassword 用户密码
     * @param roleName     角色名
     * @return {@link CommonResult}
     */
    @RequestMapping("/login")
    public CommonResult login(@RequestParam String userId, String userPassword, String roleName){

        Users returnUsers;
        UserInfo userInfo = userInfoService.getUserById(userId);

        //判断是否审核
        if (userInfo == null || (!userInfo.getUserId().equals(userId))){
            return CommonResult.error(CommonResultEm.ERROR,"用户名不存在");
        }else if (userInfo.getRegistStatus() == 0 || userInfo.getRegistStatus() == 1){
            return CommonResult.error(CommonResultEm.Unauthoried,"等待管理员审核");
        }else if (userInfo.getRegistStatus() == 3){
            return CommonResult.error(CommonResultEm.Forbidden,"账号审核不通过");
        }else if (userInfo.getRegistStatus() == 4) {
            return CommonResult.error(CommonResultEm.Forbidden,"账号已被禁用，请联系管理员");
        }

        Subject currentUser = SecurityUtils.getSubject();
        //该id用户salt值对输入密码加密
        String password = new SimpleHash("md5", userPassword,userInfo.getUserSalt(),2).toString();

        UsernamePasswordToken token = new UsernamePasswordToken(userId,password);
        try{
            currentUser.login(token);
            currentUser.getSession().setAttribute("currentUser",currentUser.getPrincipal());
            currentUser.getSession().setTimeout(24*60*60*1000L);

            returnUsers = (Users) currentUser.getPrincipal();
            Map<Integer,String> roleMap = returnUsers.getRoleNames();

            if (!roleMap.containsValue(roleName)){
                return CommonResult.error(CommonResultEm.ERROR,"您无权使用该角色，请选择您的角色！");
            }
            //更新最高角色id
            userInfoService.updateCurrentRoleId(userInfo,roleMap);

            int roleId = roleService.getIdByName(roleName).getRoleId();
            returnUsers.setCurrentRoleId(roleId);

            return CommonResult.success("用户：" + returnUsers.getUserId() + " 登陆成功，角色："
                    + roleMap.get(returnUsers.getCurrentRoleId()));

        }catch (UnknownAccountException uae){
            logger.info("用户" + userId + "不存在");
            return CommonResult.error(CommonResultEm.ERROR,"用户不存在");
        }catch (IncorrectCredentialsException ice){
            logger.info("用户" + userId + "密码错误");
            return CommonResult.error(CommonResultEm.ERROR,"用户密码错误");
        }catch (LockedAccountException lae){
            logger.info("用户" + userId + "被锁定");
            return CommonResult.error(CommonResultEm.ERROR,"用户账号被锁定");
        }catch (AuthenticationException ae){
            logger.info("登陆失败，请重试");
            return CommonResult.error(CommonResultEm.ERROR,"登陆失败");
        }
    }

    /**
     * 注销
     *
     * @param request 请求
     * @return {@link CommonResult}
     */
    @RequestMapping("/logout")
    public CommonResult logout(HttpServletRequest request){

        Subject currentUser = SecurityUtils.getSubject();
        Users users = (Users) currentUser.getPrincipal();
        if(users == null || users.getUserId() == null || users.getUserId().equals("")){
            logger.info("用户登录状态异常");
            return CommonResult.error(CommonResultEm.Unauthoried,"用户登录异常，请重新登录");
        }

        UserInfo userInfo = userInfoService.getUserById(users.getUserId());
        if (userInfoService.setLastLoginIp(userInfo,request)){
            currentUser.logout();
            return CommonResult.success("成功退出账号");
        }else{
            return CommonResult.error(CommonResultEm.ERROR,"获取IP失败");
        }
    }

    /**
     * 获取所有角色
     *
     * @return {@link CommonResult}
     */
    @RequestMapping("/getAllRoles")
    public CommonResult getAllRoles(){
        Map<Integer, String> roleNames = roleService.getAllRoles();
        if (roleNames.size() == 0 || roleNames == null){
            return CommonResult.error(CommonResultEm.NOT_EXIST,"不存在角色名称");
        }
        return CommonResult.success(roleNames);
    }

    /**
     * 获得登录前个人信息
     *
     * @param userInfo 用户信息
     * @return {@link CommonResult}
     */
    @RequestMapping("/getPersonalBeforeLogin")
    public CommonResult getLoginPersonal(@RequestBody UserInfo userInfo){
        String userId = userInfo.getUserId();
        Users user = userService.getUserById(userId);
        if (user == null || user.getUserId().length() == 0 || !userId.equals(user.getUserId())){
            return CommonResult.error(CommonResultEm.NOT_EXIST,"未找到该用户");
        }
        LoginUsersDto info = new LoginUsersDto();
        info.setUserId(user.getUserId());
        info.setRoleNames(user.getRoleNames());

        return CommonResult.success(info);
    }


    //测试
    @RequestMapping("/testUser")
    public CommonResult test(){
        Subject currentUser = SecurityUtils.getSubject();
        System.out.println(currentUser);
        Users users = (Users) currentUser.getPrincipal();
        System.out.println(users);

        return CommonResult.success("成功！");
    }
}
