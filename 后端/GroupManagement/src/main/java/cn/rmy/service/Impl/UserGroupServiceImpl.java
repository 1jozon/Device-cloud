package cn.rmy.service.Impl;

import cn.rmy.common.beans.groupManager.*;
import cn.rmy.common.pojo.dto.SelectResult;
import cn.rmy.dao.GroupToGroupDao;
import cn.rmy.dao.UserGroupDao;
import cn.rmy.dao.UserTDao;
import cn.rmy.dao.UserToGroupDao;
import cn.rmy.service.UserGroupService;
import cn.rmy.service.UserTService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserGroupServiceImpl implements UserGroupService {

    @Autowired
    private UserGroupDao userGroupDao;

    @Autowired
    private UserToGroupDao userToGroupDao;

    @Autowired
    private GroupToGroupDao groupToGroupDao;

    @Autowired
    private UserTService userTService;

    @Autowired
    private UserTDao userTDao;


    @Override
    public int insert(UserGroup group) {
        QueryWrapper<UserGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("group_name",group.getGroupName());
        UserGroup userGroup = userGroupDao.selectOne(wrapper);
        int rec;
        UserT userT = userTService.getUserByUserId(group.getCreatorId());
        if(userT == null) return -2;
        if(userGroup==null){
            rec = userGroupDao.insert(group);
            UserGroup temp = userGroupDao.selectOne(wrapper);
            UserToGroup userToGroup = new UserToGroup();
            if(temp!=null){
                userToGroup.setGroupId(temp.getId());
                userToGroup.setUsId(userT.getId());
                userToGroupDao.insert(userToGroup);
            }

        }
        else rec = -1;
        return rec;
    }

    @Override
    public int update(UserGroup group) {
        if(userGroupDao.selectById(group.getId())==null) return -1;

        QueryWrapper<UserGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("group_name",group.getGroupName());
        if(userGroupDao.selectOne(wrapper)!=null) return 2;

        UserGroup userGroup = userGroupDao.selectById(group.getId());
        userGroup.setGroupName(group.getGroupName());
        int rec = userGroupDao.updateById(userGroup);
        return rec;
    }

    @Override
    public int delete(Integer id) {
        if(userGroupDao.selectById(id)==null) return -1;
        int rec = 1;
        QueryWrapper<UserToGroup> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("group_id",id);
        rec *= userToGroupDao.delete(wrapper1);

        QueryWrapper<GroupToGroup> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("us_group_id",id);
        rec *= groupToGroupDao.delete(wrapper2);

        rec *= userGroupDao.deleteById(id);
        return rec;
    }

    @Override
    public List<UserGroup> getGroupByCondition(UserGroup group) {
        QueryWrapper<UserGroup> wrapper = new QueryWrapper<>();
        if(group.getGroupName()!=null)
            wrapper.like("group_name",group.getGroupName());
        List<UserGroup> list = userGroupDao.selectList(wrapper);
        return list;
    }

    @Override
    public SelectResult getAllGroup(int current, int size) {
        current = current<=0?1:current;
        size = size<=0?4:size;
        Page<UserGroup> page = new Page<>(current,size);
        QueryWrapper<UserGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        userGroupDao.selectPage(page,queryWrapper);
        List<UserGroup> list = page.getRecords();
        return new SelectResult(page.getTotal(), list);
    }

    @Override
    public List<UserGroup> getGroupByUserId(UserT userT) {
        List<UserGroup> list = new ArrayList<>();
        if(userT.getUserId()==null||userT.getUserId().length()==0)
            list = userGroupDao.selectList(null);
        else{
            QueryWrapper<UserT> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("user_id",userT.getUserId());
            if(userTDao.selectOne(queryWrapper1)!=null){
                UserT temp = userTDao.selectOne(queryWrapper1);
                QueryWrapper<UserToGroup> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("us_id",temp.getId());
                List<UserToGroup> l = userToGroupDao.selectList(queryWrapper2);
                if(l!=null&&l.size()>0){
                    for(UserToGroup t:l){
                        list.add(userGroupDao.selectById(t.getGroupId()));
                    }
                }
            }
        }

        return list;
    }
}
