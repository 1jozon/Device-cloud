package cn.rmy.service;

import cn.rmy.common.beans.groupManager.InstToGroup;
import cn.rmy.common.pojo.dto.InstGroupVO;

import java.util.List;

public interface InstToGroupService {
    int insert(List<InstToGroup> list);
    int delete(InstToGroup instToGroup);
    List<InstGroupVO> getInstByGroup(Integer groupId);
}
