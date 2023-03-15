package cn.rmy.service;

import cn.rmy.common.beans.groupManager.UserGroup;
import cn.rmy.common.beans.groupManager.UserT;
import cn.rmy.common.pojo.dto.SelectResult;

import java.util.List;

public interface UserGroupService {
    int insert(UserGroup group);
    int update(UserGroup group);
    int delete(Integer id);
    List<UserGroup> getGroupByCondition(UserGroup group);
    SelectResult getAllGroup(int current,int size);
    List<UserGroup> getGroupByUserId(UserT userT);
}
