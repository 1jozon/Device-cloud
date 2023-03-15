package cn.rmy.controller;

import cn.rmy.common.dto.Role;
import cn.rmy.common.dto.SelectConditionDto;
import cn.rmy.common.dto.Users;
import cn.rmy.common.pojo.dto.SelectResult;
import cn.rmy.common.redisUtils.CommonResult;
import cn.rmy.common.ResultEnums.CommonResultEm;
import cn.rmy.common.redisUtils.LogAnno;
import cn.rmy.otherFunc.DownLoad;
import cn.rmy.service.imp.*;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
//@RequiresRoles(value = {"管理员","系统管理员"}, logical = Logical.OR)
@RequestMapping("rmy/super")
public class SuperManagerController {
    Logger logger = LoggerFactory.getLogger(SuperManagerController.class);

    @Autowired
    private UserInfoServiceImp userInfoService;

    @Autowired
    private UserRoleServiceImp userRoleService;

    @Autowired
    private RoleServiceImp roleService;

    @Autowired
    private RoleInsTypeServiceImp roleInsTypeService;

    //数据库配置信息
    //数据库地址
    @Value("${spring.datasource.url}")
    String url;

    //用户名
    @Value("${spring.datasource.username}")
    String userName;

    //密码
    @Value("${spring.datasource.password}")
    String password;

    //数据库名称
    @Value("rmy")
    //@Value("${dbNames}")
    String dbNames;

    //保存路径
    @Value("C:\\Users\\Administrator\\Desktop\\file\\db\\")
    //@Value("${dbFilePath}")
    String dbFilePath;

    @Value("127.0.0.1")
    String host;
    @Value("C:\\Users\\Administrator\\Desktop\\file\\")
    String resource;

    /**
     * 注册审批
     *
     * @param user 用户
     * @return {@link CommonResult}
     */
    @LogAnno(operateType = "进行注册用户审批")
    @RequestMapping("/registApprove")
    public CommonResult registApprove(@RequestBody Users user){
        String userId = user.getUserId();
        int registStatus = user.getRegistStatus();
        int rec = userInfoService.registerApprove(userId, registStatus);
        if (rec == 1){
            return CommonResult.error(CommonResultEm.ERROR,"用户不存在");
        }else if(rec == -1){
            return  CommonResult.error(CommonResultEm.ERROR,"审批失败");
        }else if(rec == 2){
            return CommonResult.error(CommonResultEm.ERROR,"审批状态码错误");
        }else if(rec == 3){
            return CommonResult.error(CommonResultEm.ERROR,"该用户已被审批");
        }
        return CommonResult.success("审批成功");
    }

    /**
     * 批量审核准
     *
     * @return {@link CommonResult}
     */
    @LogAnno(operateType = "批量审批所有注册用户")
    @RequestMapping("/batchApprove")
    public CommonResult batchApprove(){
        int rec = userInfoService.getRegistUsers();
        return CommonResult.success("一键审批成功");

    }

    /**
     * 删除用户及角色
     *
     * @param condition 条件
     * @return {@link CommonResult}
     */
    @LogAnno(operateType = "删除用户")
    @RequestMapping("/deleteUserById")
    public CommonResult deleteUserById(@RequestBody SelectConditionDto condition){

        if (condition.getUserId() == null || condition.getUserId().length() <= 0){
            return CommonResult.error(CommonResultEm.ERROR,"请求失败，参数错误");
        }
        int recUser =userInfoService.deleteUserById(condition.getUserId());
        if (recUser == -1) {
            return CommonResult.error(CommonResultEm.ERROR, "用户不存在");
        }else if(recUser == -2){
            return CommonResult.error(CommonResultEm.ERROR,"删除用户失败");
        }

        int recRole = userRoleService.deleteByUserId(condition.getUserId());
        if (recRole == -1){
            return CommonResult.error(CommonResultEm.ERROR,"删除角色失败");
        }

        return CommonResult.success("删除成功");
    }

    /**
     * 数据库备份
     *
     * @param request  请求
     * @param response 响应
     * @throws UnsupportedEncodingException 不支持的编码异常
     */
    public void exportSQL(HttpServletRequest request, HttpServletResponse response)throws UnsupportedEncodingException {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
        String fileName = date + ".sql";
        try{
            if (exportDatabaseTool(host,userName,password,dbFilePath,fileName,dbNames)){
                System.out.println("数据库备份成功");
            }else {
                System.out.println("数据库备份失败");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DownLoad.downloadFile(dbFilePath,fileName,request,response);
    }

    /**
     * 导出数据库数据
     *
     * @param hostIP       主机ip
     * @param userName     用户名
     * @param password     密码
     * @param savePath     保存路径
     * @param fileName     文件名称
     * @param databaseName 数据库名称
     * @return boolean
     * @throws InterruptedException 中断异常
     */
    public static boolean exportDatabaseTool(String hostIP, String userName,String password,String savePath,String fileName, String databaseName) throws InterruptedException{
        File saveFile = new File(savePath);
        //若目录不存在，创建文件夹
        if(!saveFile.exists()){
            saveFile.mkdirs();
        }
        if (!savePath.endsWith(File.separator)){
            savePath = savePath + File.separator;
        }

        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        try{
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(savePath + fileName), "utf8"));
            //备份指定表
//            Process process = Runtime.getRuntime().exec("mysqldump remoteservice -h" + hostIP + " -u" + userName + " -p" + password +" --set-charset=UTF8 " + "--tables t_area");
            //备份整个库
            Process process = Runtime.getRuntime().exec("mysqldump -h" + hostIP + " -u" + userName + " -p" + password + " --set-charset=UTF8 " + databaseName);
            //备份除xxx表以外
//            Process process = Runtime.getRuntime().exec("mysqldump -h" + hostIP + " -u" + userName + " -p" + password + " --set-charset=UTF8 " + "--ignore-table=" + databaseName + ".t_area " + databaseName);
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                printWriter.println(line);
                printWriter.flush();
            }
            //0 表示线程正常终止
            if (process.waitFor() == 0) {
                return true;
            }
            System.out.println("code" + process.waitFor());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 条件搜索角色
     *
     * @param current   当前的
     * @param size      大小
     * @param condition 条件
     * @return {@link CommonResult}
     */
    @RequestMapping("/selectRoleInfo/{current}/{size}")
    public CommonResult selectRoleInfo(@PathVariable("current") int current, @PathVariable("size") int size,
                                       @RequestBody SelectConditionDto condition){
        SelectResult selectResult = roleService.getRolesInfo(current,size,condition);
        if (selectResult.getTotal() == 0){
            return CommonResult.error(CommonResultEm.ERROR,"未找到该角色");
        }
        return CommonResult.success(selectResult);
    }
    /**
     * 添加角色
     *
     * @param role 角色
     * @return {@link CommonResult}
     */
    @LogAnno(operateType = "添加新角色")
    @RequestMapping("/insertRole")
    public CommonResult insertRole(@RequestBody Role role){
        String roleName = role.getRoleName();
        String roleDescribe = role.getRoleDescribe();
        int rec = roleService.insert(roleName, roleDescribe);

        if (rec == 0){
            return CommonResult.error(CommonResultEm.ERROR,"添加失败");
        }else if(rec == 2){
            return CommonResult.error(CommonResultEm.ERROR,"该角色已存在，请勿重复添加");
        }
        return CommonResult.success("添加成功");
    }

    /**
     * 更新角色信息
     *
     * @param newRole 新角色
     * @return {@link CommonResult}
     */
    @LogAnno(operateType = "更新角色信息")
    @RequestMapping("/updateRole")
    public CommonResult updateRole(@RequestBody Role newRole){
        if (newRole.getRoleName().length() == 0){
            return CommonResult.error(CommonResultEm.ERROR,"请输入正确的用户名称");
        }
        int rec = roleService.update(newRole);

        if (rec == 1){
           return CommonResult.success("修改成功");
        }else {
           return CommonResult.error(CommonResultEm.ERROR,"修改失败");
        }
    }

    /**
     * 本月获得角色类型
     * <p>
     * 得到角色所有仪器权限类型
     *
     * @param role 角色
     * @return {@link CommonResult}
     */
    @RequestMapping("/getRoleInsTypes")
    public CommonResult getRoleInstTypes(@RequestBody Role role){
        String roleName = role.getRoleName();

        List<String> allInsTypes = roleInsTypeService.allInsTypes(roleName);

        if (allInsTypes == null || allInsTypes.size() == 0){
            return CommonResult.error(CommonResultEm.ERROR,"未找到该角色权限内的仪器类型");
        }
        return CommonResult.success(allInsTypes);
    }

    /**
     * 删除角色
     *
     * @param role 角色
     * @return {@link CommonResult}
     */
    @LogAnno(operateType = "删除角色")
    @RequestMapping("/deleteRole")
    public CommonResult deleteRole(@RequestBody Role role){

        int rec = roleService.delete(role.getRoleName());
        if (rec != 1){
            return CommonResult.error(CommonResultEm.ERROR, "删除失败");
        }
        return CommonResult.success("删除成功");
    }
}
