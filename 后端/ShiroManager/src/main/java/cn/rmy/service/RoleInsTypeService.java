package cn.rmy.service;

import cn.rmy.common.beans.shiroUsers.RoleInsTypeInfo;

import java.util.List;

public interface RoleInsTypeService {

    /**
     * 添加信息
     *
     * @param roleName 角色名
     * @param insTypes ins类型
     * @return int
     */
    int addInfo(String roleName, List<String> insTypes);

    /**
     * 删除
     *
     * @param roleName 角色名
     * @param insTypes ins类型
     * @return int
     */
    int delete(String roleName, List<String> insTypes);

    /**
     * 得到
     *
     * @param roleName 角色名
     * @param insType  ins类型
     * @return {@link RoleInsTypeInfo}
     */
    RoleInsTypeInfo get(String roleName, String insType);

    /**
     * 所有ins类型
     *
     * @param roleName 角色名
     * @return {@link List}<{@link String}>
     */
    List<String> allInsTypes(String roleName);
}
