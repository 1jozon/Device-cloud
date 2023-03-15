package cn.rmy.service;

import cn.rmy.common.beans.shiroUsers.RolePermissionInfo;

import java.util.List;

public interface RolePermissionService {
    /**
     * 通过角色id获取权限
     *
     * @param roleId 角色id
     * @return {@link List}<{@link RolePermissionInfo}>
     */
    List<RolePermissionInfo> getByRoleId(int roleId);
}
