package cn.rmy.dao;

import cn.rmy.common.beans.shiroUsers.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户信息映射器
 *
 * @author chu
 * @date 2022/06/07
 */
@Mapper
public interface UsersInfoMapper {

    /**
     * 导出的条件查询-申请审批用户
     *
     * @param roleId   角色id
     * @return {@link List}<{@link UserInfo}>
     */
    List<UserInfo> exportRegisteGetByCondition(@Param("roleId") int roleId);

    List<UserInfo> exportGetByCondition(@Param("roleId") int roleId);
}
