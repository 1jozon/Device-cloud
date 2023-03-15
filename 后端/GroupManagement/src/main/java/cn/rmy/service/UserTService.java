package cn.rmy.service;

import cn.rmy.common.beans.groupManager.UserT;

public interface UserTService {
    int insert(UserT userT);
    UserT getUserByUserId(String userId);
}
