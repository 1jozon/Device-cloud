package cn.rmy.service;


import cn.rmy.common.beans.shiroUsers.UserInfo;
import cn.rmy.common.dto.Users;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UsersService {
    /**
     * id得到用户
     *
     * @param userId 用户id
     * @return {@link Users}
     */
    Users getUserById(String userId);

    /**
     * 获取当前用户insId列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link String}>
     */
    List<String> getcurrentUserInsIdList(String userId);

    /**
     * 得到所有ins id
     * 得到所有ins id
     *
     * @return {@link List}<{@link String}>
     */
    List<String> getAllInsId();

}
