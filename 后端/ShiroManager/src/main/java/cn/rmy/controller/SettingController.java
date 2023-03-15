package cn.rmy.controller;

import cn.rmy.common.beans.shiroUsers.UserInfo;
import cn.rmy.common.dto.UpdatePassword;
import cn.rmy.common.dto.UpdateUser;
import cn.rmy.common.dto.Users;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.redisUtils.LogAnno;
import cn.rmy.service.imp.UserInfoServiceImp;
import cn.rmy.service.imp.UsersServiceImp;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("rmy/set")
//@RequiresRoles(value = {"访客","售后","销售","研发","测试","质量","管理员","系统管理员"}, logical = Logical.OR)
public class SettingController {

    private Logger logger = LoggerFactory.getLogger(SettingController.class);

    @Autowired
    private UserInfoServiceImp userInfoService;

    @Autowired
    private UsersServiceImp userService;


    /**
     * 获取个人信息
     *
     * @return {@link CommonResult}
     */
    @RequestMapping("/getPersonal")
    public CommonResult getPersonal(){
        Users users = new Users();
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Users temp = (Users) currentUser.getPrincipal();
            users = userService.getUserById(temp.getUserId());
            if (users == null){
                return CommonResult.error(CommonResultEm.NOT_EXIST,"用户不存在");
            }
            users.setUserSalt("");
            users.setUserPassword("");
            users.setUserAvatar(users.getUserAvatar());
            users.setRoles(null);

        }catch (Exception e){
            logger.info("用户登录状态异常");
            return CommonResult.error(CommonResultEm.Unauthoried,"用户登录异常，请重新登录");
        }
        return CommonResult.success(users);
    }

    /**
     * 获取个人信息
     *
     * @return {@link CommonResult}
     */
    @RequestMapping("/getPersonalByMini")
    public CommonResult getPersonalByMini(@RequestBody UpdateUser updateUser){
        if (updateUser == null || updateUser.getUserId() == null || updateUser.getUserId().equals("")){
            return CommonResult.error(CommonResultEm.NOT_Acceptable,"请指定用户UserId");
        }
        Users users = new Users();
        try {
            users = userService.getUserById(updateUser.getUserId());
            if (users == null){
                return CommonResult.error(CommonResultEm.NOT_EXIST,"用户不存在");
            }
            users.setUserSalt("");
            users.setUserPassword("");
            users.setUserAvatar(users.getUserAvatar());
            users.setRoles(null);

        }catch (Exception e){
            logger.info("用户登录状态异常");
            return CommonResult.error(CommonResultEm.Unauthoried,"用户登录异常，请重新登录");
        }
        return CommonResult.success(users);
    }

    /**
     * 更新个人信息
     *
     * @param updateUser 更新用户
     * @return {@link CommonResult}
     */
    @RequestMapping("/updatePersonal")
    public CommonResult updatePersonal(@RequestBody UpdateUser updateUser){

        int rec = userInfoService.updateUser(updateUser);
        switch (rec){
            case -1: return CommonResult.error(CommonResultEm.ERROR,"修改失败，验证码不存在或失效");

            case 0:  return CommonResult.error(CommonResultEm.ERROR,"修改失败，验证码不存在或错误");

            case 1: return CommonResult.success("修改成功");

            case 2: return CommonResult.error(CommonResultEm.ERROR,"修改失败，信息更新出错");
        }
        return CommonResult.error(CommonResultEm.ERROR,"未知错误");

    }

    /**
     * 更改密码
     *
     * @param updatePassword 更新密码
     * @return {@link CommonResult}
     */
    @RequestMapping("/changePassword")
    public CommonResult changePassword(@RequestBody UpdatePassword updatePassword){
        Subject currentUser = SecurityUtils.getSubject();
        Users temp = (Users) currentUser.getPrincipal();

        UserInfo userInfo = userInfoService.getUserById(updatePassword.getUserId());

        if (!(userInfo.getUserId().equals(temp.getUserId()))){
            return CommonResult.error(CommonResultEm.ERROR,"请修改当前用户名");
        }

        userInfo.setUserSalt(temp.getUserSalt());
        String password = new SimpleHash("md5",updatePassword.getOldPassword(),temp.getUserSalt(),2).toString();

        if( !(password.equals(temp.getUserPassword()))){
            System.out.println(userInfo);
            System.out.println("密码错误");
            return CommonResult.error(CommonResultEm.ERROR,"密码错误");
        }
        int rec = userInfoService.changePwd(userInfo,updatePassword.getNewPassword());
        if (rec == 0){
            currentUser.logout();
            return CommonResult.success("修改成功，请重新登录");
        }else {
            return CommonResult.error(CommonResultEm.ERROR,"修改失败");
        }
    }

    /**
     * 更改角色
     *
     * @param userInfo 用户信息
     * @return {@link CommonResult}
     */
    @LogAnno(operateType = "更改用户角色")
    @RequestMapping("/changeRole")
    public CommonResult changeRole(@RequestBody UserInfo userInfo){
        int roleId = userInfo.getRoleId();
        Subject currentUser = SecurityUtils.getSubject();
        Users users = (Users) currentUser.getPrincipal();

        if (users.getCurrentRoleId() == roleId){
            return CommonResult.error(CommonResultEm.ERROR,"请切换到新角色");
        }
        Map<Integer, String> roleMap = users.getRoleNames();
        if (!roleMap.containsKey(roleId)){
            CommonResult.error(CommonResultEm.ERROR,"您没有权限切换到该角色");
        }

        users.setCurrentRoleId(roleId);
        int rec = userInfoService.updateCurrentRole(users);
        if (rec == -1){
            return CommonResult.error(CommonResultEm.ERROR,"切换角色失败");
        }
        currentUser.logout();
        return CommonResult.success("更换角色成功，请重新登陆");
    }

    /**
     * 更改用户头像
     *
     * @param file 文件
     * @return {@link CommonResult}
     */
    @RequestMapping("/changeAvatar")
    public CommonResult changeAvatar(@RequestParam("file") MultipartFile file) {
        Subject currentUser = SecurityUtils.getSubject();
        Users users = (Users) currentUser.getPrincipal();

        int rec = userInfoService.changeAvatar(users,file);
        if (rec == -1){
            return CommonResult.error(CommonResultEm.ERROR,"上传头像失败");
        }else if (rec == -2){
            return CommonResult.error(CommonResultEm.ERROR,"修改失败，上传文件格式错误");
        }else if(rec == -3){
            return CommonResult.error(CommonResultEm.ERROR,"修改失败，添加数据库出错");
        }

        return CommonResult.success("上传成功");
    }

    /**
     * 获取验证码
     *
     * @param updateUser 更新用户
     * @return {@link CommonResult}
     */
    @RequestMapping("/getVerificationCode")
    public CommonResult getVerificationCode(@RequestBody UpdateUser updateUser){
        int code = userInfoService.getVerificationCode(updateUser);
        if (code >= 100000){
            return CommonResult.success("成功");
        }else{
            return CommonResult.error(CommonResultEm.ERROR,"验证码生成失败");
        }
    }

}
