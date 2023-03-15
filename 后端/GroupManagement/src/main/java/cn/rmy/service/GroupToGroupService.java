package cn.rmy.service;

import cn.rmy.common.beans.groupManager.GroupToGroup;
import cn.rmy.common.pojo.dto.GroupToGroupVO;

import java.util.List;

public interface GroupToGroupService {
    int insertGTG(GroupToGroup gtg);
    int updateGTG(GroupToGroup gtg);
    int deleteGTG(Integer id);

    //查询分组
    List<GroupToGroupVO> getByGroupId(GroupToGroup gtg);
}
