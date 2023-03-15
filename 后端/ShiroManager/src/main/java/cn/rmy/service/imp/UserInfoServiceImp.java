package cn.rmy.service.imp;

import cn.rmy.common.beans.articleGps.PicturesInfo;
import cn.rmy.common.beans.shiroUsers.UserInfo;
import cn.rmy.common.beans.shiroUsers.UserRoleInfo;
import cn.rmy.common.dto.*;
import cn.rmy.common.pojo.dto.SelectResult;
import cn.rmy.common.pojo.dto.emaildto.MailDto;
import cn.rmy.common.redisUtils.CommonUtil;
import cn.rmy.dao.PictureInfoDao;
import cn.rmy.dao.UserRoleDao;
import cn.rmy.dao.UsersDao;
import cn.rmy.dao.UsersInfoMapper;
import cn.rmy.emailUtil.SendMailUtil;
import cn.rmy.fileUtils.OssFileUtils;
import cn.rmy.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserInfoServiceImp implements UserInfoService {

    @Autowired
    public UsersDao usersDao;

    @Autowired
    private UsersInfoMapper usersInfoMapper;

    @Autowired
    public PictureInfoDao pictureInfoDao;

    @Autowired
    public UserInfoServiceImp userInfoService;

    @Autowired
    public OssFileUtils ossFileUtils;

    @Autowired
    public UsersServiceImp usersService;

    @Autowired
    private SendEmailMsgServiceImp emailService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public int insertUser(UserInfo userInfo) {
        if (userInfo == null){
            return -1;  //添加失败
        }else if(getUserById(userInfo.getUserId()) != null){
            return 1; //用户已存在
        }else{

            //插入默认头像
            QueryWrapper<PicturesInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("picture_title", "默认头像");
            PicturesInfo picturesInfo = pictureInfoDao.selectOne(queryWrapper);
            if(picturesInfo == null || picturesInfo.getPictureUrl() == null || picturesInfo.getPictureUrl().length() == 0){
                picturesInfo = null;
            }

            UserInfo user = new UserInfo();
            user.setUserId(userInfo.getUserId())
                    .setUserName(userInfo.getUserName())
                    .setUserGender(userInfo.getUserGender())
                    .setUserPassword(userInfoService.createUserInfoByMD5(userInfo))
                    .setUserPhone(userInfo.getUserPhone())
                    .setUserEmail(userInfo.getUserEmail())
                    .setRegistStatus(userInfo.getRegistStatus())
                    .setRoleId(userInfo.getRoleId())
                    .setUserSalt(userInfo.getUserSalt())
                    .setUserAvatar(picturesInfo != null ? picturesInfo.getPictureUrl() : null);
            int rec = usersDao.insert(user);
            if (rec == 1){
                return 0;  //添加成功
            }else{
                return -1;  //添加失败
            }
        }

    }

    @Override
    public int deleteUserById(String userId) {
        //用户不存在

        //System.out.println(userId + "user.length()=" + userId.length() + "\nuser:" + getUserById(userId));

        if (userId.length() == 0 || getUserById(userId) == null) {
            return -1;
        }
        int id = getUserById(userId).getId();
        System.out.println(id);

        int rec = usersDao.deleteById(id);


        if (rec == 1){
            return 1;  //删除成功
        }else{
            return -2; //删除失败
        }
    }

    /**
     * 更新用户信息
     * 邮箱、手机号
     *
     * @param updateUser 更新用户
     * @return int
     */
    @Override
    public int updateUser(UpdateUser updateUser) {
        if (updateUser == null){
            return 0;
        }else{
            String userId = updateUser.getUserId();
            String email = updateUser.getUserEmail();
            UserInfo user = getUserById(userId);

            //验证码验证
            ValueOperations<String, String> forValue = stringRedisTemplate.opsForValue();
            String key;
            String oldVerificationCode;
            if (email != null && email.length() != 0){
                user.setUserEmail(email);
                key = userId + "mailCode";
            }else{
                user.setUserPhone(updateUser.getUserPhone());
                key = userId + "phoneCode";
            }
            oldVerificationCode = forValue.get(key);
            if (oldVerificationCode == null) {
                //验证码不存在或已过期
                return 0;
            }else if(oldVerificationCode.equals(updateUser.getVerificationCode())){
                //验证码正确\更新数据失败
                int rec = usersDao.updateById(user);
                if (rec == 1){
                    stringRedisTemplate.delete(key);
                    return 1;
                }else{
                    return 2;
                }
            }else{
                //验证码错误
                return -1;
            }
        }
    }

    @Override
    public UserInfo getUserById(String userId) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<UserInfo> userInfos = usersDao.selectList(queryWrapper);
        if (userInfos.size() == 0){
            return null;
        }
        UserInfo userInfo = userInfos.get(0);
        return userInfo;
    }

    @Override
    public int resetPwdById(String userId) {

        UserInfo userInfo = getUserById(userId);
        //重置密码 == userId
        if (userInfo == null){
            return 0;
        }
        //String password = userInfoService.createUserInfoByMD5(userInfo);
        String password = new SimpleHash("md5",userInfo.getUserId(),userInfo.getUserSalt(),2).toString();
        userInfo.setUserPassword(password);

        int rec = usersDao.updateById(userInfo);
        if (rec == 1) {
            return 1;  //重置成功
        }else{
            return -1;  //重置失败
        }
    }

    @Override
    public int registerApprove(String userId, int status) {
        if (userId.length() == 0 || getUserById(userId) == null){
            return 1;  //用户不存在
        }
        if (status != 2 && status != 3 && status != 4){
            return 2;  //注册状态异常
        }
        UserInfo userInfo = getUserById(userId);
/*        if (userInfo.getRegistStatus() != 0 ){
            return 3;  //注册已审批
        }*/
        userInfo.setRegistStatus(status);
        int rec = usersDao.updateById(userInfo);
        if (rec == 1) {
            //邮件通知
            emailService.sendApproveEmailMsg(userInfo);
            return 0;  //成功
        }else {
            return -1;  //失败
        }
    }

    @Override
    public int updateCurrentRole(Users users)
    {
        if (users.getCurrentRoleId() < 0 || users.getCurrentRoleId() > 7){
            return -1;
        }
        if (getUserById(users.getUserId()) == null){
            return -1;
        }
        UserInfo userInfo = getUserById(users.getUserId());
        userInfo.setRoleId(users.getCurrentRoleId());
        int rec = usersDao.updateById(userInfo);
        if (rec == 1){
            return 0;  //成功
        }else{
            return -1;
        }
    }

    @Override
    public int changePwd(UserInfo userInfo, String newPassword) {
        if (getUserById(userInfo.getUserId()) == null){
            return -1;
        }
        //定义userName数据为新密码
        userInfo.setUserPassword(newPassword);
        UserInfo userInfo1 = getUserById(userInfo.getUserId());
        userInfo1.setUserPassword(userInfoService.createUserInfoByMD5(userInfo));
        int rec = usersDao.updateById(userInfo1);
        if (rec == 1){
            return 0;
        }else{
            return -1;
        }
    }

    @Override
    public int changeAvatar(Users users, MultipartFile file) {
        String userId = users.getUserId();
        //文件上传
        if (file != null){
            //获取上传文件名称
            String fileName = file.getOriginalFilename();
            if(StringUtils.isNotBlank(fileName)){
                //获取后缀名
                String suffix = ossFileUtils.getExtension(file);
                if (suffix.equalsIgnoreCase("png")
                        && suffix.equalsIgnoreCase("jpg")
                        && suffix.equalsIgnoreCase("jpeg")){
                    //上传失败，文件格式不对
                    return -2;
                }
                try {
                    //获取加密url
                    String url = ossFileUtils.uploadFile(file,0,0, null);
                    UserInfo userInfo = userInfoService.getUserById(userId);
                    boolean rec = ossFileUtils.deleteFile(userInfo.getUserAvatar());
                    userInfo.setUserAvatar(url);
                    usersDao.updateById(userInfo);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else{
            return -1;  //上传失败，文件为空
        }
        return 1;
    }

    @Override
    public SelectResult selectByCondition(UserInfo userInfo, int current, int size) {
        System.out.println("分页查询");
        if (current <= 0){
            current = 1;
        }
        if (size <= 0){
            size = 4;
        }

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        String userId = "";
        String userName = "";
        int roleId = userInfo.getRoleId();
        int registStatus = userInfo.getRegistStatus();
        if (userInfo.getUserId() != null){
            userId = userInfo.getUserId();
        }
        if (userInfo.getUserName() != null){
            userName = userInfo.getUserName();
        }

        if (roleId == 0 && registStatus != 0){
            queryWrapper.like("user_id",userId)
                    .like("user_name",userName)
                    .eq("regist_status",registStatus);
        }else if (roleId != 0 && registStatus != 0){
            queryWrapper.like("user_id",userId)
                    .like("user_name",userName)
                    .like("role_id",roleId)
                    .eq("regist_status",registStatus);
        }else if (roleId == 0 && registStatus == 0){
            queryWrapper.like("user_id",userId)
                    .like("user_name",userName)
                    .eq("regist_status",registStatus);
        }else{
            queryWrapper.like("role_id",roleId)
                    .like("user_id",userId)
                    .like("user_name",userName)
                    .eq("regist_status",registStatus);
        }

        Page<UserInfo> page = new Page<>(current,size);
        usersDao.selectPage(page,queryWrapper);
        List<UserInfo> records =page.getRecords();
        if (records != null){
            for (UserInfo record : records){
                record.setUserPassword(null)
                        .setUpdateTime(null)
                        .setVersion(null)
                        .setDeleted(null)
                        .setCreateTime(null);
            }
        }
        System.out.println("分页查询结束");
        long total = page.getTotal();
        System.out.println(total + "--" + current + "--" + size);
        return new SelectResult(total,records);

    }

    /**
     * 查询用户
     *
     * @param userInfo 用户信息
     * @param regist   regist 0-所有用户、1-审批用户
     * @return {@link List}<{@link UserInfo}>
     */
    @Override
    public List<UserInfo> selectByCondition(UserInfo userInfo, int regist) {
        long start = System.currentTimeMillis();
        if (userInfo.getRoleId() > 0 && regist == 1){
            //审批用户角色查询
            List<UserInfo> userInfoList = usersInfoMapper.exportRegisteGetByCondition(userInfo.getRoleId());
            System.out.println("角色查询非审批耗时：");
            System.out.println(System.currentTimeMillis() - start);
            return userInfoList;
        }else if(userInfo.getRoleId() > 0 ){
            List<UserInfo> userInfoList = usersInfoMapper.exportGetByCondition(userInfo.getRoleId());
            System.out.println("角色查询非审批耗时：");
            System.out.println(System.currentTimeMillis() - start);
            return userInfoList;
        }else{
            //非角色用户
            QueryWrapper<UserInfo>queryWrapper = new QueryWrapper<>();
            if(userInfo.getUserId() != null){
                queryWrapper.like("user_id", userInfo.getUserId());
            }
            if (userInfo.getUserName() != null){
                queryWrapper.like("user_name", userInfo.getUserName());
            }
            if (userInfo.getUserPhone() != null){
                queryWrapper.like("user_phone", userInfo.getUserPhone());
            }
            if (regist == 1){
                queryWrapper.eq("regist_status", 1);
            }else{
                queryWrapper.ge("regist_status", 0);
            }
            queryWrapper.eq("deleted", 0);
            List<UserInfo> records = usersDao.selectList(queryWrapper);
            return records;
        }

    }

    @Override
    public String createUserInfoByMD5(UserInfo userInfo) {

        //随机生成salt值，并通过用户注册的密码和salt值经两次md5算法生成真实存储的密码
        String password = new SimpleHash("md5",userInfo.getUserPassword(),userInfo.getUserSalt(),2).toString();
        return password;
    }

    @Override
    public boolean setLastLoginIp(UserInfo userInfo, HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        if (ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }

        userInfo.setLastLoginIp(ip);
        int rec = usersDao.updateById(userInfo);
        if (rec == 1){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 分页搜索用户列表
     *
     * @param current   当前的
     * @param size      大小
     * @param condition 条件
     * @return {@link SelectResult}
     */
    @Override
    public SelectResult getUsersByCondition(int current, int size, SelectConditionDto condition) {
        if(current <= 0 || CommonUtil.isNull(current)){
            current = 1;
        }
        if (size <= 0 || CommonUtil.isNull(size)){
            size = 4;
        }
        Page<UserInfo> page = new Page<>(current, size);
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        if(condition.getRegistStatus() == 1){
            queryWrapper.eq("regist_status",1);
        }if (condition.getUserId() != null && condition.getUserId().length() > 0){
            queryWrapper.like("user_id", condition.getUserId());
        }if (condition.getUserName() != null && condition.getUserName().length() > 0){
            queryWrapper.like("user_name", condition.getUserName());
        }if (condition.getUserPhone() != null && condition.getUserPhone().length() > 0){
            queryWrapper.like("user_phone", condition.getUserPhone());
        }if (condition.getRoleId() > 0){
            queryWrapper.like("role_id", condition.getRoleId());
        }
        else {
            //全部用户
            queryWrapper.eq("deleted", 0).orderByDesc("create_time");
        }
        usersDao.selectPage(page, queryWrapper);
        List<UserInfo> userInfoList = page.getRecords();
        long total = page.getTotal();
        if (userInfoList.size() == 0){
            return new SelectResult((long) 0, userInfoList);
        }
        List<Users> usersList = new ArrayList<>();
        for (UserInfo info : userInfoList){
            Users user = usersService.getUserById(info.getUserId());
            if (CommonUtil.isNotNull(user)){
                user.setUserPassword(null);
                user.setRoles(null);
                user.setUserSalt(null);
                user.setCurrentRoleId(-1);
                usersList.add(user);
            }else{
                continue;
            }
        }
        return new SelectResult(total, usersList);
    }

    /**
     * 一键审批
     *
     * @return {@link List}<{@link UserInfo}>
     */
    @Override
    public int getRegistUsers() {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("regist_status", 1)
                .eq("deleted", 0);

        List<UserInfo> list = usersDao.selectList(queryWrapper);
        if (list == null || list.size() == 0){
            return 1;
        }
        for (UserInfo info : list){
            info.setRegistStatus(2);
            int rec = usersDao.updateById(info);
            if (rec == 1){
                emailService.sendApproveEmailMsg(info);
                continue;
            }
        }
        return 1;
    }

    /**
     * 获取验证码
     *
     * @param updateUser 更新用户
     * @return int
     */
    @Override
    public int getVerificationCode(UpdateUser updateUser) {
        String userId = updateUser.getUserId();
        String key;

        UserInfo user = getUserById(userId);
        //获取6位随机数
        Integer verificationCode = (int)((Math.random() * 9 + 1) * 100000);
        String codeStr = verificationCode.toString();
        if (updateUser.getUserEmail() != null && updateUser.getUserEmail().length() != 0){
            key = userId + "mailCode";
            //对旧邮箱进行发送验证
            sendVerificationCode(0,verificationCode,user.getUserId(),user.getUserEmail());
        }else {
            //修改手机
            key = userId + "phoneCode";
            sendVerificationCode(1,verificationCode,user.getUserId(),user.getUserEmail());
        }
        try {
            //将验证码保存在redis，有效时间为5分钟
            ValueOperations<String, String> forValue = stringRedisTemplate.opsForValue();
            forValue.set(key, codeStr);
            stringRedisTemplate.expire(key, 5, TimeUnit.MINUTES);
        }catch (Exception e){
            e.printStackTrace();
        }
        return verificationCode;
    }

    /**
     * 发送验证码到邮箱
     * 邮箱、手机号修改验证码
     *
     * @param type         类型 0-邮件、1-手机号
     * @param code         代码
     * @param userId       用户id
     * @param emailAddress 电子邮件地址
     */
    @Override
    public void sendVerificationCode(int type, int code, String userId, String emailAddress) {
        MailDto mail = new MailDto();
        String content;
        mail.setSubject("RMY验证码");
        if (type == 0){
            content = "尊敬的"+userId+"您好：\n"
                    + "您正在进行修改使用邮件地址操作，请确认是本人操作，您的验证码为："
                    + code +",有效期为5分钟，请您及时处理。";
        }else{
            content = "尊敬的"+userId+"您好：\n"
                    + "您正在进行修改使用手机号操作，请确认是本人操作，您的验证码为："
                    + code +",有效期为5分钟，请您及时处理。";
        }
        mail.setContent(content);
        String[] emails = {emailAddress};
        mail.setTos(emails);
        SendMailUtil.send(mail);

    }

    /**
     * 更新当前最大角色id和用户头像
     *
     * @param userInfo 用户信息
     * @param roleMap
     */
    @Override
    public void updateCurrentRoleId(UserInfo userInfo, Map<Integer, String> roleMap) {
        int maxRoleId = 0;
        Set<Integer> keys = roleMap.keySet();
        for (Integer key : keys){
            maxRoleId = Math.max(maxRoleId, key);
        }
        userInfo.setRoleId(maxRoleId);
        if (userInfo.getUserAvatar() == null || userInfo.getUserAvatar().length() == 0){
            //插入默认头像
            QueryWrapper<PicturesInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("picture_title", "默认头像");
            PicturesInfo picturesInfo = pictureInfoDao.selectOne(queryWrapper);
            if(picturesInfo == null || picturesInfo.getPictureUrl() == null || picturesInfo.getPictureUrl().length() == 0){
                picturesInfo = null;
            }

            userInfo.setUserAvatar(picturesInfo != null ? picturesInfo.getPictureUrl() : null);
        }
        usersDao.updateById(userInfo);
    }

    /**
     * 得到维护的用户
     *
     * @param condition 条件
     * @return {@link SelectResult}
     */
    @Override
    public SelectResult getMaintenceUsers(SelectConditionDto condition) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_id", condition.getUserId());
        List<UserInfo> infoList = usersDao.selectList(queryWrapper);
        if(infoList == null || infoList.size() == 0){
            return null;
        }

        List<MaintenceUserDto> maintenceUserDtoList = new ArrayList<>();
        for(UserInfo info : infoList){
            MaintenceUserDto maintence = new MaintenceUserDto();
            maintence.setUserId(info.getUserId());
            maintence.setUserName(info.getUserName());
            maintence.setUserPhone(info.getUserPhone());
            maintenceUserDtoList.add(maintence);
        }
        Long total = new Long(infoList.size());
        SelectResult selectResult = new SelectResult(total, maintenceUserDtoList);
        return selectResult;
    }
}
