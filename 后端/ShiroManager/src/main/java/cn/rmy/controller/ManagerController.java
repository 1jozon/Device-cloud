package cn.rmy.controller;


import cn.rmy.common.dto.SelectConditionDto;
import cn.rmy.common.dto.Users;
import cn.rmy.common.beans.shiroUsers.UserInfo;
import cn.rmy.common.dto.UpdateUser;
import cn.rmy.common.pojo.dto.SelectResult;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.redisUtils.LogAnno;
import cn.rmy.otherFunc.DownLoad;
import cn.rmy.otherFunc.ExcelToList;
import cn.rmy.otherFunc.ListToExcel;
import cn.rmy.service.imp.SendEmailMsgServiceImp;
import cn.rmy.service.imp.UserInfoServiceImp;
import cn.rmy.service.imp.UserRoleServiceImp;
import cn.rmy.service.imp.UsersServiceImp;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 管理员控制器
 *
 * @author chu
 * @date 2021/11/11
 */
@RestController
//@RequiresRoles(value = {"管理员","系统管理员"}, logical = Logical.OR)
@RequestMapping("rmy/manager")
public class ManagerController {
    private final static Logger logger = LoggerFactory.getLogger(ManagerController.class);

     @Value("${filePath}")
    private String filePath;

    @Autowired
    private UsersServiceImp userService;

    @Autowired
    private UserRoleServiceImp userRoleService;

    @Autowired
    private UserInfoServiceImp userInfoService;

    @Autowired
    private SendEmailMsgServiceImp emailService;


    /**
     * 重置通过id
     *
     * @param users 用户
     * @return {@link CommonResult}
     */
    @RequestMapping("/resetPassById")
    public CommonResult resetPassById(@RequestBody Users users){
        if (users == null ||users.getUserId() == null || users.getUserId().length() == 0){
            return CommonResult.error(CommonResultEm.ERROR,"请输入用户名userId");
        }
        int recUser = userInfoService.resetPwdById(users.getUserId());
        if (recUser == 1){
            return CommonResult.success("密码重置为账号");
        }else if(recUser == 0){
            return CommonResult.error(CommonResultEm.ERROR,"用户不存在");
        }
        return CommonResult.error(CommonResultEm.ERROR,"重置密码失败");
    }

    /**
     * 查看用户申请
     *
     * @param userInfo 用户信息
     * @param current  当前的
     * @param pageSize 页面大小
     * @return {@link CommonResult}
     */
    @RequestMapping("/getUserInfoByCondition/{current}/{size}")
    public CommonResult getUserInfoByCondition(@RequestBody UserInfo userInfo
            , @PathVariable("current") int current
            , @PathVariable("size") int pageSize){
        SelectResult selectResult = userInfoService.selectByCondition(userInfo,current,pageSize);
        List<UserInfo> userInfos = (List<UserInfo>) selectResult.getList();
        if (userInfos == null){
            return CommonResult.error(CommonResultEm.ERROR,"未查到用户");
        }

        return CommonResult.success(new SelectResult(selectResult.getTotal(),userInfos));
    }

    /**
     * 分配角色
     *
     * @param updateUser 更新用户
     * @return {@link CommonResult}
     */
    @LogAnno(operateType = "给用户分配角色")
    @RequestMapping("/assignRoles")
    public CommonResult assignRoles(@RequestBody UpdateUser updateUser) throws Exception{

        /*UserInfo userInfo = new UserInfo();
        userInfo.setUserId(updateUser.getUserId());
        userInfo.setUserName(updateUser.getUserName());
        userInfo.setUserPhone(updateUser.getUserPhone());

        int recUser = userInfoService.updateUser(userInfo);
        if (recUser == -1){
            CommonResult.error(CommonResultEm.ERROR,"个人信息更新失败");
        }*/
        Subject currentUser = SecurityUtils.getSubject();
        Users users = (Users) currentUser.getPrincipal();
        if(updateUser.getRoleIds() ==  null || updateUser.getRoleIds().length == 0){
            return CommonResult.error(CommonResultEm.ERROR,"请指定分配的角色");
        }
        if(users.getCurrentRoleId() == 7){
            for(int roleId : updateUser.getRoleIds()){
                if(roleId == 8){
                    return CommonResult.success("您无权对系统管理员操作");
                }
            }
        }

        //int recRole = userRoleService.insert(updateUser.getUserId(),updateUser.getRoleIds());
        int recRole = userRoleService.assignRoles(updateUser.getUserId(), updateUser.getRoleIds());
        if (recRole == -1){
            return CommonResult.error(CommonResultEm.ERROR,"角色分配失败");
        }
        return CommonResult.success("角色分配、更改成功");
    }

    /**
     * 导出用户申请信息文件
     *
     * @param userInfo 用户信息
     * @param request  请求
     * @param response 响应
     * @throws IOException ioexception
     */
    @LogAnno(operateType = "导出用户申请列表文件")
    @RequestMapping("/exportRegisterListFile")
    public void exportRegisterListFile(@RequestBody UserInfo userInfo, HttpServletRequest request, HttpServletResponse response)throws IOException{
        List<UserInfo> userInfos = userInfoService.selectByCondition(userInfo,1);
        if (userInfos == null || userInfos.size() == 0){
            return;
        }
        String fileName = "用户审批表.xlsx";
        ListToExcel.userInfoToExcel(filePath,fileName,userInfos);
        DownLoad.downloadFile(filePath,fileName,request,response);
        logger.info("用户申请列表导出");
    }

    /**
     * 导出用户列表文件
     *
     * @param userInfo 用户信息
     * @param request  请求
     * @param response 响应
     * @throws IOException ioexception
     */
    @LogAnno(operateType = "导出用户列表文件")
    @RequestMapping("/exportUserListFile")
    public void exportUserListFile(@RequestBody UserInfo userInfo, HttpServletRequest request, HttpServletResponse response)throws IOException{
        List<UserInfo> userInfos = userInfoService.selectByCondition(userInfo,0);
        if (userInfos == null || userInfos.size() == 0){
            return;
        }
        String fileName = "用户信息表.xlsx";
        ListToExcel.userInfoToExcel(filePath,fileName,userInfos);
        DownLoad.downloadFile(filePath,fileName,request,response);
        logger.info("用户申请列表导出");
    }

    /**
     * 获取注册表模板
     *
     * @param request  请求
     * @param response 响应
     * @throws IOException ioexception
     */
    @RequestMapping("/getRegistTemp")
    public void getRegistTemp(HttpServletRequest request,HttpServletResponse response)throws IOException{
        String fileName = "registerTemplate.xlsx";
        ListToExcel.userToExcel(filePath,fileName,null);
        DownLoad.downloadFile(filePath,fileName,request,response);
    }

    /**
     * 批量注册
     *
     * @return {@link CommonResult}
     */
    @LogAnno(operateType = "批量用户注册")
    @RequestMapping("/batchRegister")
    public CommonResult batchRegister(){
        String fileName = "registUsers.xlsx";
        List<UserInfo> userInfos = ExcelToList.excelToUserInfo(filePath,fileName);
        for (UserInfo userInfo : userInfos){
            int rec = userInfoService.insertUser(userInfo);
            int recRole = userRoleService.insert(userInfo.getUserId(),new int[]{userInfo.getRoleId()});
            //邮件通知
            emailService.sendRegistEmailMsg(userInfo);
        }
        return CommonResult.success("注册成功");
    }

    /**
     * 条件搜索用户列表
     *
     * @param current   当前的
     * @param size      大小
     * @param condition 条件
     * @return {@link CommonResult}
     */
    @RequestMapping("/getUsersByCondition/{current}/{size}")
    public CommonResult getUsersByCondition(@PathVariable("current") int current,
                                            @PathVariable("size") int size,
                                            @RequestBody SelectConditionDto condition){

        SelectResult selectResult = new SelectResult();
        if (condition.getRoleName()== null){
            selectResult = userInfoService.getUsersByCondition(current,size,condition);
        }else{
            selectResult = userRoleService.getUsersByRoleName(current,size,condition);
        }

        if (selectResult.getTotal() == 0){
            return CommonResult.success("未查询到结果");
        }else{
            return CommonResult.success(selectResult);
        }
    }

    @RequestMapping("/getMaintenceUsers")
    public CommonResult getMaintenceUsers(@RequestBody SelectConditionDto condition){
        if(condition.getUserId() == null || condition.getUserId().length() == 0){
            return CommonResult.error(CommonResultEm.REQ_PARAM_IS_ERROR, "请输入用户Id");
        }
        SelectResult selectResult = new SelectResult();
        selectResult = userInfoService.getMaintenceUsers(condition);

        if (selectResult.getTotal() == 0){
            return CommonResult.error(CommonResultEm.ERROR, "未查询到结果");
        }else{
            return CommonResult.success(selectResult);
        }
    }

}
