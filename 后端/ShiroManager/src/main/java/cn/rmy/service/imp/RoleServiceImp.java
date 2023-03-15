package cn.rmy.service.imp;

import cn.rmy.common.beans.shiroUsers.RoleInfo;
import cn.rmy.common.beans.shiroUsers.RoleInsTypeInfo;
import cn.rmy.common.dto.Role;
import cn.rmy.common.dto.SelectConditionDto;
import cn.rmy.common.pojo.dto.SelectResult;
import cn.rmy.dao.RoleDao;
import cn.rmy.dao.RoleInstypeDao;
import cn.rmy.service.RoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class RoleServiceImp implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleInstypeDao roleInstypeDao;

    @Autowired
    private RoleInsTypeServiceImp roleInsTypeService;

    /**
     * 通过角色id获取角色
     *
     * @param roleId 角色id
     * @return {@link Role}
     */
    @Override
    public Role getByRoleId(int roleId) {
        RoleInfo roleInfo = roleDao.selectById(roleId);
        if(roleInfo == null){
            return null;
        }
        //permission
        //insType
        List<String> insTypeList = new ArrayList<>();
        QueryWrapper<RoleInsTypeInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name", roleInfo.getRoleName()).groupBy("ins_type");
        List<RoleInsTypeInfo> roleInsTypeInfoList = roleInstypeDao.selectList(queryWrapper);
        if (roleInsTypeInfoList == null || roleInsTypeInfoList.size() == 0){
            insTypeList = null;
        }else{
            for (RoleInsTypeInfo info : roleInsTypeInfoList){
                if (info.getInsType().length() == 0){
                    continue;
                }
                insTypeList.add(info.getInsType());
            }
        }

        Role role = new Role(roleInfo.getRoleId(), roleInfo.getRoleName(), roleInfo.getRoleDescribe(), null
                , insTypeList,roleInfo.getCreateTime());
        return role;
    }

    /**
     * 根据名字获得roleId
     *
     * @param roleName 角色名
     * @return int
     */
    @Override
    public RoleInfo getIdByName(String roleName) {
        QueryWrapper<RoleInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name",roleName);
        RoleInfo roleInfo = roleDao.selectOne(queryWrapper);

        return roleInfo;
    }

    /**
     * 把所有角色
     * 获取所有角色类型
     *
     * @return {@link Map}<{@link Integer}, {@link String}>
     */
    @Override
    public Map<Integer, String> getAllRoles() {
        List<RoleInfo> list = new ArrayList<>();
        //QueryWrapper<RoleInfo> queryWrapper = new QueryWrapper<>();
        //queryWrapper.groupBy("role_name");
        //queryWrapper.groupBy("role_name");
        //list = roleDao.selectList(queryWrapper);

        list = roleDao.getAllRoleName();

        if (list.size() == 0 || list == null){
            return null;
        }
        Map<Integer, String> nameList = new HashMap<>();
        for (RoleInfo roleInfo : list){
            nameList.put(roleInfo.getRoleId(),roleInfo.getRoleName());
        }
        return nameList;
    }

    /**
     * 条件搜索角色信息
     *
     * @param current   当前的
     * @param size      大小
     * @param condition 条件
     * @return {@link SelectResult}
     */
    @Override
    public SelectResult getRolesInfo(int current, int size, SelectConditionDto condition) {
        if(current <= 0){
            current = 1;
        }
        if (size <= 0){
            size = 4;
        }

        Page<RoleInfo> page = new Page<>(current,size);
        QueryWrapper<RoleInfo> queryWrapper = new QueryWrapper<>();
        if (condition.getAll() == 1){
            queryWrapper.eq("deleted", 0).orderByAsc("role_id");
        }else if(condition.getRoleName() != null && condition.getRoleName().length() > 0){
            queryWrapper.like("role_name",condition.getRoleName());
        }else{
            return null;
        }
        roleDao.selectPage(page, queryWrapper);
        List<RoleInfo> roleInfoList = page.getRecords();
        List<Role> roleList = new ArrayList<>();
        if (roleInfoList == null || roleInfoList.size() == 0){
            return new SelectResult((long)0, roleInfoList);
        }
        for (RoleInfo info : roleInfoList){
            Role role = getByRoleId(info.getRoleId());
            if (role == null){
                continue;
            }
            roleList.add(role);
        }
        return new SelectResult((long) page.getTotal(), roleList);

    }



    /**
     * 插入
     *
     * @param roleName     角色名
     * @param roleDescribe 角色描述
     * @return int
     */
    @Override
    public int insert(String roleName, String roleDescribe) {
        Map<Integer, String> roles = getAllRoles();
        if (roles.containsValue(roleName)){
            return 2;
        }
        if (roleName.length() == 0){
            return 0;
        }

        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRoleName(roleName)
                .setRoleDescribe(roleDescribe);
        int rec = roleDao.insert(roleInfo);

        return rec == 1 ? 1 : 0;
    }

    /**
     * 更新
     *
     * @param newRole 新角色
     * @return int
     */
    @Override
    public int update(Role newRole) {
        String roleName = newRole.getRoleName();
        RoleInfo oldRoleInfo = getIdByName(roleName);
        oldRoleInfo.setRoleDescribe(newRole.getRoleDescribe());
        int rec = roleDao.updateById(oldRoleInfo);

        List<String> oldIntypes = roleInsTypeService.allInsTypes(roleName);
        List<String> newInsTypes = newRole.getInsTypes();
        if (oldIntypes == null && newInsTypes == null){
            return 1;
        }else if(oldIntypes != null && newInsTypes != null){
            int oldCount = oldIntypes.size();


            for (String old : oldIntypes){
                int newCount = newInsTypes.size();
                for (String newTypes : newInsTypes){
                    if (old.equals(newTypes)){
                        oldIntypes.remove(old);
                        newInsTypes.remove(old);
                    }
                    newCount --;
                    if (newInsTypes.size() == 0|| newCount == 0){
                        break;
                    }
                }
                oldCount--;
                if (oldIntypes.size() == 0 || oldCount == 0){
                    break;
                }
            }
        }
        if(newInsTypes != null) {
            roleInsTypeService.addInfo(roleName, newInsTypes);
        }
        if(oldIntypes != null){
            roleInsTypeService.delete(roleName, oldIntypes);
        }
        return 1;
    }

    /**
     * 删除角色
     *
     * @param roleName 角色名
     * @return int
     */
    @Override
    public int delete(String roleName) {
        QueryWrapper<RoleInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name", roleName);
        int rec = roleDao.delete(queryWrapper);

        return rec == 1 ? 1 : 0;
    }
}
