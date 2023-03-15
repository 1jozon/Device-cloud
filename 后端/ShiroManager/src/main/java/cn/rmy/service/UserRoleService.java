package cn.rmy.service;

import cn.rmy.common.dto.SelectConditionDto;
import cn.rmy.common.dto.UserRole;
import cn.rmy.common.pojo.dto.SelectResult;

import java.util.List;

public interface UserRoleService {
    /**
     * 通过用户id获取角色
     *
     * @param userId 用户id
     * @return {@link List}<{@link UserRole}>
     */
    List<UserRole> getByUserId(String userId);

    /**
     * 插入角色
     *
     * @param userId  用户id
     * @param roleIds 角色id
     * @return int
     */
    int insert(String userId, int[] roleIds);

    /**
     * 分配角色
     *
     * @param userId  用户id
     * @param roleIds 角色id
     * @return int
     */
    int assignRoles(String userId, int[] roleIds) throws Exception;

    /**
     * 按用户id删除角色
     *
     * @param userId 用户id
     * @return int
     */
    int deleteByUserId(String userId);

    /**
     * 得到用户角色名
     * 角色名搜索
     *
     * @param current   当前的
     * @param size      大小
     * @param condition 条件
     * @return {@link SelectResult}
     */
    SelectResult getUsersByRoleName(int current, int size, SelectConditionDto condition);

}
