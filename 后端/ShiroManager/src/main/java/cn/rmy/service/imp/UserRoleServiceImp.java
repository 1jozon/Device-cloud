package cn.rmy.service.imp;

import cn.rmy.common.beans.shiroUsers.RoleInfo;
import cn.rmy.common.beans.shiroUsers.UserRoleInfo;
import cn.rmy.common.dto.SelectConditionDto;
import cn.rmy.common.dto.Users;
import cn.rmy.common.dto.UserRole;
import cn.rmy.common.pojo.dto.SelectResult;
import cn.rmy.dao.RoleDao;
import cn.rmy.dao.UserRoleDao;
import cn.rmy.service.UserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户角色服务
 *
 * @author chu
 * @date 2021/11/11
 */
@Service
@Transactional
public class UserRoleServiceImp implements UserRoleService {

    @Autowired
    public UserRoleDao userRoleDao;

    @Autowired
    public RoleDao roleDao;

    @Autowired
    public UsersServiceImp usersService;

    @Override
    public List<UserRole> getByUserId(String userId) {

        QueryWrapper<UserRoleInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<UserRoleInfo> userRoleInfos = userRoleDao.selectList(queryWrapper);
        List<UserRole> userRoles = new ArrayList<>();
        if (userRoleInfos.size() > 0){
            for (UserRoleInfo userRoleInfo : userRoleInfos){
                UserRole userRole =new UserRole(userRoleInfo.getId(),userRoleInfo.getUserId(),userRoleInfo.getRoleId());
                userRoles.add(userRole);
            }
        }
        return userRoles;
    }


    @Override
    public int insert(String userId, int[] roleIds) {

        QueryWrapper<UserRoleInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        //所有该Id用户
        List<UserRoleInfo> userRolesInfos = userRoleDao.selectList(queryWrapper);
        //获取该用户所有存在id
        Set<Integer> exitRolesId = new HashSet<>();
        //需要添加的户权限
        List<UserRoleInfo> insertUserRoles = new ArrayList<>();

        //获得该用户已有角色id
        if (userRolesInfos.size() > 0){
            for (UserRoleInfo userRoleInfo : userRolesInfos){
                exitRolesId.add(userRoleInfo.getRoleId());
            }
        }
        //不存在该id用户角色 且 需要添加角色
        else if(roleIds.length > 0){
            for (int roleId : roleIds){
                if (roleId < 0 || roleId > 8){
                    continue;
                }
                //创建该id用户关系，
                UserRoleInfo userRoleInfo = new UserRoleInfo();
                userRoleInfo.setRoleId(roleId);
                userRoleInfo.setUserId(userId);
                insertUserRoles.add(userRoleInfo);
            }
        }
        //存在该用户角色,加入未存在角色
        if (exitRolesId.size() > 0 && roleIds.length > 0){
            for (int roleId : roleIds){
                if (!(exitRolesId.contains(roleId))){
                    if (roleId < 0 || roleId > 8){
                        continue;
                    }
                    UserRoleInfo userRoleInfo = new UserRoleInfo();
                    userRoleInfo.setUserId(userId);
                    userRoleInfo.setRoleId(roleId);
                    insertUserRoles.add(userRoleInfo);
                }
            }
        }
        //加入数据库中
        if (insertUserRoles.size() > 0){
            for (UserRoleInfo userRoleInfo : insertUserRoles)
            {
                int rec = userRoleDao.insert(userRoleInfo);
                if (rec != 1){
                    return -1;
                }
            }
        }
        return 0;
    }

    /**
     * 分配角色
     *
     * @param userId  用户id
     * @param roleIds 角色id
     * @return int
     * @throws Exception 异常
     */
    @Override
    public int assignRoles(String userId, int[] roleIds) throws Exception {
        QueryWrapper<UserRoleInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        //所有该Id用户
        List<UserRoleInfo> userRolesInfos = userRoleDao.selectList(queryWrapper);
        //List<UserRoleInfo> noNeedDeletedUserRole = new ArrayList<>();
        List<UserRoleInfo> needDeletedUserRole = new ArrayList<>();

        try{
            for(UserRoleInfo info : userRolesInfos){
                boolean sameFlag = false;
                for(int i = 0; i < roleIds.length; i++){
                    if(info.getRoleId() == roleIds[i]){
                        sameFlag = true;
                        roleIds[i] = -1;
                    }
                }
                if(sameFlag == false){
                    needDeletedUserRole.add(info);
                }
            }
            //去除角色
            if(needDeletedUserRole != null && needDeletedUserRole.size() > 0){
                QueryWrapper<UserRoleInfo> queryWrapper1 = new QueryWrapper<>();
                ArrayList<Integer> deletedId = new ArrayList<>();
                for(UserRoleInfo info : needDeletedUserRole){
                    deletedId.add(info.getId());
                }
                queryWrapper.in("id", deletedId);
                userRoleDao.delete(queryWrapper);
            }
            //添加角色
            if(roleIds.length > 0){
                for (int i = 0; i < roleIds.length; i++){
                    if(roleIds[i] > 0 && roleIds[i] < 9){
                        UserRoleInfo newInfo = new UserRoleInfo();
                        newInfo.setUserId(userId);
                        newInfo.setRoleId(roleIds[i]);
                        userRoleDao.insert(newInfo);
                    }
                }
            }
        }catch (Exception e){
            e.getCause();
            return -1;
        }
        return 1;
    }

    @Override
    public int deleteByUserId(String userId) {
        QueryWrapper<UserRoleInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        int rec = userRoleDao.delete(queryWrapper);
        if (rec == 0){
            return -1;  //失败
        }
        return 0;  //成功
    }


    /**
     * 角色名搜索
     *
     * @param current  当前的
     * @param size     大小
     * @param condition
     * @return {@link SelectResult}
     */
    @Override
    public SelectResult getUsersByRoleName(int current, int size , SelectConditionDto condition) {
        int roleId = 0;

        List<Users> usersList = new ArrayList<>();
        QueryWrapper<RoleInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name", condition.getRoleName());
        RoleInfo roleinfo = roleDao.selectOne(queryWrapper);

        if (roleinfo != null){
            roleId = roleinfo.getRoleId();
            QueryWrapper<UserRoleInfo> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("role_id",roleId);
            List<UserRoleInfo> userRoleInfos = userRoleDao.selectList(queryWrapper1);

            if (userRoleInfos.size() == 0){
                return new SelectResult((long) 0, usersList);
            }else {
                current = current-1;
                for(int i=current*size;i<Math.min(current*size+size,userRoleInfos.size());i++){
                    Users user = usersService.getUserById(userRoleInfos.get(i).getUserId());
                    user.setRoles(null);
                    user.setUserPassword(null);
                    user.setUserSalt(null);
                    user.setCurrentRoleId(-1);
                    usersList.add(user);
                }
                return new SelectResult((long) userRoleInfos.size(), usersList);
                /* 优化前
                for (UserRoleInfo info : userRoleInfos){
                    Users user = usersService.getUserById(info.getUserId());
                    if (user == null || (condition.getRegistStatus() == 1 && user.getRegistStatus() != 1)){
                        continue;
                    }else{

                        user.setRoles(null);
                        user.setUserPassword(null);
                        user.setUserSalt(null);
                        user.setCurrentRoleId(-1);
                        usersList.add(user);
                    }
                }*/
            }
        }else{
            return new SelectResult((long) 0, usersList);
        }
    }
}
