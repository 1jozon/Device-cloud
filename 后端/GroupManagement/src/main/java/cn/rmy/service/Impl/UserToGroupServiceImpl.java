package cn.rmy.service.Impl;

import cn.rmy.common.beans.groupManager.UserToGroup;
import cn.rmy.common.pojo.dto.UserGroupVO;
import cn.rmy.dao.UserToGroupDao;
import cn.rmy.dao.dtoDao.UserGroupVODao;
import cn.rmy.service.UserToGroupService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserToGroupServiceImpl implements UserToGroupService {

    @Autowired
    private UserToGroupDao userToGroupDao;

    @Autowired
    private UserGroupVODao userGroupVODao;

    @Override
    public int insert(List<UserToGroup> list) {
        int rec=1;
        for(UserToGroup group : list){
            QueryWrapper<UserToGroup> wrapper = new QueryWrapper<>();
            wrapper.eq("us_id",group.getUsId())
                    .eq("group_id",group.getGroupId());
            if(userToGroupDao.selectOne(wrapper)!=null){
                continue;
                //return -1;
            }
            rec = userToGroupDao.insert(group);
            if(rec==0) return 0;
        }
        return rec;
    }

    @Override
    public int delById(UserToGroup userToGroup) {
        int rec;
        if(userToGroupDao.selectById(userToGroup.getId())==null) return -1;
        rec = userToGroupDao.deleteById(userToGroup.getId());
        return rec;
    }

    @Override
    public List<UserGroupVO> getUserByGroup(Integer groupId) {
        List<UserGroupVO> list = userGroupVODao.getUserByGroupId(groupId);
        return list;
    }

    @Override
    public List<String> getGroupNameByUserId(Integer usId) {

        return userGroupVODao.getGroupNameByUserId(usId);
    }


}
