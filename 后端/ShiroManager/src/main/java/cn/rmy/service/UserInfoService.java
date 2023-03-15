package cn.rmy.service;

import cn.rmy.common.beans.shiroUsers.UserInfo;
import cn.rmy.common.dto.SelectConditionDto;
import cn.rmy.common.dto.UpdateUser;
import cn.rmy.common.dto.Users;
import cn.rmy.common.pojo.dto.SelectResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface UserInfoService {

    /**
     * 插入用户
     *
     * @param userInfo 用户信息
     * @return int
     */
    int insertUser(UserInfo userInfo);

    /**
     * 删除用户的id
     *
     * @param userId 用户id
     * @return int
     */
    int deleteUserById(String userId);

    /**
     * 更新用户
     *
     * @param updateUser 更新用户
     * @return int
     */
    int updateUser(UpdateUser updateUser);

    /**
     * userId得到用户
     *
     * @param userId 用户id
     * @return {@link UserInfo}
     */
    UserInfo getUserById(String userId);

    /**
     * 重置pwd通过userId
     *
     * @param userId 用户id
     * @return int
     */
    int resetPwdById(String userId);

    /**
     * 注册批准
     *
     * @param userId 用户id
     * @param status 状态
     * @return int
     */
    int registerApprove(String userId, int status);

    /**
     * 更新当前的角色
     *
     * @param users 用户
     * @return int
     */
    int updateCurrentRole(Users users);

    /**
     * 改变pwd
     *
     * @param userInfo    用户信息
     * @param newPassword 新密码
     * @return int
     */
    int changePwd(UserInfo userInfo, String newPassword);

    /**
     * 修改头像
     *
     * @param users 用户
     * @param file  文件
     * @return int
     */
    int changeAvatar(Users users, MultipartFile file);

    /**
     * 分页条件查询用户
     *
     * @param userInfo 用户信息
     * @param current  当前的
     * @param size     大小
     * @return {@link SelectResult}
     */
    SelectResult selectByCondition(UserInfo userInfo, int current, int size);

    /**
     * 查询用户
     *
     * @param userInfo 用户信息
     * @param regist   regist 0-所有用户、1-审批用户
     * @return {@link List}<{@link UserInfo}>
     */
    List<UserInfo> selectByCondition(UserInfo userInfo,int regist);

    /**
     * 创建用户信息通过md5
     *
     * @param userInfo 用户信息
     * @return {@link String}
     */
    String createUserInfoByMD5(UserInfo userInfo);

    /**
     * 设置最后登录IP
     *
     * @param userInfo 用户信息
     * @param request  请求
     * @return boolean
     */
    boolean setLastLoginIp(UserInfo userInfo,HttpServletRequest request);


    /**
     * 分页搜索用户列表
     *
     * @param current   当前的
     * @param size      大小
     * @param condition 条件
     * @return {@link SelectResult}
     */
    SelectResult getUsersByCondition(int current, int size, SelectConditionDto condition);

    /**
     * 得到regist用户
     *
     * @return int
     */
    int getRegistUsers();

    /**
     * 获取验证码
     *
     * @param updateUser 更新用户
     * @return int
     */
    int getVerificationCode(UpdateUser updateUser);

    /**
     * 发送验证码
     * 邮箱、手机号修改验证码
     *
     * @param type         类型 0-邮件、1-手机号
     * @param code         代码
     * @param userId       用户id
     * @param emailAddress 电子邮件地址
     */
    void sendVerificationCode(int type, int code, String userId, String emailAddress);

    /**
     * 更新当前最大角色id
     *  @param userInfo  用户信息
     * @param roleMap
     */
    void updateCurrentRoleId(UserInfo userInfo, Map<Integer,String> roleMap);

    /**
     * 得到维护的用户
     *
     * @param condition 条件
     * @return {@link SelectResult}
     */
    SelectResult getMaintenceUsers(SelectConditionDto condition);
}
