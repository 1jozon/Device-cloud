package cn.rmy.service;

import cn.rmy.common.beans.Instrument;
import cn.rmy.common.beans.groupManager.InstGroup;
import cn.rmy.common.pojo.dto.SelectResult;

import java.util.List;

public interface InstGroupService {
    int insert(InstGroup group);
    int update(InstGroup group);
    int delete(Integer id);
    List<InstGroup> getGroupByCondition(InstGroup group);
    SelectResult getAllGroup(int current, int size);

    List<InstGroup> getGroupByInstId(Instrument instrument);
}
