package cn.rmy.service;

import cn.rmy.common.beans.groupManager.UserToGroup;
import cn.rmy.common.pojo.dto.UserGroupVO;

import java.util.List;

public interface UserToGroupService {
    int insert(List<UserToGroup> list);
    int delById(UserToGroup userToGroup);

    List<UserGroupVO> getUserByGroup(Integer groupId);

    List<String> getGroupNameByUserId(Integer usId);

}
