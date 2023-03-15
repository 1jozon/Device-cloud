package cn.rmy.service.imp;

import cn.rmy.common.beans.shiroUsers.RolePermissionInfo;
import cn.rmy.dao.RolePermissionDao;
import cn.rmy.service.RolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色权限服务
 *
 * @author chu
 * @date 2021/11/11
 */
@Service
@Transactional
public class RolePermissionServiceImp implements RolePermissionService {

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    public List<RolePermissionInfo> getByRoleId(int roleId) {
        QueryWrapper<RolePermissionInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleId);
        List<RolePermissionInfo> rolePermissionInfos = rolePermissionDao.selectList(queryWrapper);
        return rolePermissionInfos;
    }
}
