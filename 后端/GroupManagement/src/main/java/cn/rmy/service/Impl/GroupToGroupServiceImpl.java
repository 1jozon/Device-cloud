package cn.rmy.service.Impl;

import cn.rmy.common.beans.groupManager.GroupToGroup;
import cn.rmy.common.pojo.dto.GroupToGroupVO;
import cn.rmy.dao.GroupToGroupDao;
import cn.rmy.dao.dtoDao.GroupToGroupVODao;
import cn.rmy.service.GroupToGroupService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GroupToGroupServiceImpl implements GroupToGroupService {

    @Autowired
    private GroupToGroupDao groupToGroupDao;

    @Autowired
    private GroupToGroupVODao groupToGroupVODao;

    @Override
    public int insertGTG(GroupToGroup gtg) {
        QueryWrapper<GroupToGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("us_group_id",gtg.getUsGroupId())
                .eq("inst_group_id",gtg.getInstGroupId());
        int rec;
        if(groupToGroupDao.selectOne(wrapper)!=null) return -1;
        else{
            rec = groupToGroupDao.insert(gtg);
        }
        return rec;
    }

    @Override
    public int updateGTG(GroupToGroup gtg) {
        int rec;
        QueryWrapper<GroupToGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("us_group_id",gtg.getUsGroupId())
                .eq("inst_group_id",gtg.getInstGroupId());
        if(groupToGroupDao.selectOne(wrapper)!=null) return -2;
        if(groupToGroupDao.selectById(gtg.getId())==null) return -1;
        else{
            GroupToGroup groupToGroup = groupToGroupDao.selectById(gtg.getId());
            groupToGroup.setUsGroupId(gtg.getUsGroupId());
            groupToGroup.setInstGroupId(gtg.getInstGroupId());
            rec = groupToGroupDao.updateById(groupToGroup);
        }
        return rec;
    }

    @Override
    public int deleteGTG(Integer id) {
        int rec;
        if(groupToGroupDao.selectById(id)==null) return -1;
        else{
            rec = groupToGroupDao.deleteById(id);
        }
        return rec;
    }

    @Override
    public List<GroupToGroupVO> getByGroupId(GroupToGroup gtg) {
        List<GroupToGroupVO> list;
        //System.out.println(gtg);
        if(gtg.getUsGroupId()!=0 && gtg.getInstGroupId()==0){
            list = groupToGroupVODao.getGTGByUsGroupId(gtg.getUsGroupId());
        }
        else if(gtg.getUsGroupId()==0 && gtg.getInstGroupId()!=0){
            list = groupToGroupVODao.getGTGByInstGroupId(gtg.getInstGroupId());
        }
        else{
            list = groupToGroupVODao.getGTGByGroupId(gtg.getUsGroupId(),gtg.getInstGroupId());
        }
        return list;
    }
}
