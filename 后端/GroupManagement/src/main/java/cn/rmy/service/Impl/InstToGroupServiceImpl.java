package cn.rmy.service.Impl;

import cn.rmy.common.beans.groupManager.InstToGroup;
import cn.rmy.common.pojo.dto.InstGroupVO;
import cn.rmy.dao.InstToGroupDao;
import cn.rmy.dao.dtoDao.InstGroupVODao;
import cn.rmy.service.InstToGroupService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InstToGroupServiceImpl implements InstToGroupService {

    @Autowired
    private InstToGroupDao instToGroupDao;

    @Autowired
    private InstGroupVODao instGroupVODao;

    @Override
    public int insert(List<InstToGroup> list) {
        int rec=1;

        for(InstToGroup group : list){
            QueryWrapper<InstToGroup> wrapper = new QueryWrapper();
            wrapper.eq("inst_id",group.getInstId())
                    .eq("group_id",group.getGroupId());
            if(instToGroupDao.selectOne(wrapper)!=null){
                continue;
                //return -1;
            }
            rec = instToGroupDao.insert(group);
            if(rec==0) return 0;
        }
        return rec;
    }

    @Override
    public int delete(InstToGroup instToGroup) {
        int rec;
        if(instToGroupDao.selectById(instToGroup.getId())==null) return -1;
        rec = instToGroupDao.deleteById(instToGroup.getId());
        return rec;
    }

    @Override
    public List<InstGroupVO> getInstByGroup(Integer groupId) {

        List<InstGroupVO> list = instGroupVODao.getInstByGroupId(groupId);
        return list;
    }
}
