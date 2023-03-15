package cn.rmy.service;

import cn.rmy.common.beans.shiroUsers.RoleInfo;
import cn.rmy.common.dto.Role;
import cn.rmy.common.dto.SelectConditionDto;
import cn.rmy.common.pojo.dto.SelectResult;

import java.util.Map;

public interface RoleService {
    /**
     * 通过角色id
     *
     * @param roleId 角色id
     * @return {@link Role}
     */
    Role getByRoleId(int roleId);

    /**
     * 根据名字获得roleId
     *
     * @param roleName 角色名
     * @return int
     */
    RoleInfo getIdByName(String roleName);

    /**
     * 把所有角色
     * 获取所有角色类型
     *
     * @return {@link Map}<{@link Integer}, {@link String}>
     */
    Map<Integer, String> getAllRoles();

    /**
     * 条件搜索角色信息
     *
     * @param current   当前的
     * @param size      大小
     * @param condition 条件
     * @return {@link SelectResult}
     */
    SelectResult getRolesInfo(int current, int size, SelectConditionDto condition);

    /**
     * 插入
     *
     * @param roleName     角色名
     * @param roleDescribe 角色描述
     * @return int
     */
    int insert(String roleName, String roleDescribe);

    /**
     * 更新
     *
     * @param newRole 新角色
     * @return int
     */
    int update(Role newRole);

    /**
     * 删除角色
     *
     * @param roleName 角色名
     * @return int
     */
    int delete(String roleName);

}
