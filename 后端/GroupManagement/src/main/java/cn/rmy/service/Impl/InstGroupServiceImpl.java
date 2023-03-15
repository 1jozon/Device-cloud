package cn.rmy.service.Impl;

import cn.rmy.common.beans.Instrument;
import cn.rmy.common.beans.groupManager.GroupToGroup;
import cn.rmy.common.beans.groupManager.InstGroup;
import cn.rmy.common.beans.groupManager.InstToGroup;
import cn.rmy.common.pojo.dto.SelectResult;
import cn.rmy.dao.GroupToGroupDao;
import cn.rmy.dao.InstGroupDao;
import cn.rmy.dao.InstToGroupDao;
import cn.rmy.dao.InstrumentDao;
import cn.rmy.service.InstGroupService;
import cn.rmy.service.UserTService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InstGroupServiceImpl implements InstGroupService {

    @Autowired
    private InstGroupDao instGroupDao;

    @Autowired
    private InstToGroupDao instToGroupDao;

    @Autowired
    private GroupToGroupDao groupToGroupDao;

    @Autowired
    private UserTService userTService;

    @Autowired
    private InstrumentDao instrumentDao;

    @Override
    public int insert(InstGroup group) {
        QueryWrapper<InstGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("group_name",group.getGroupName());
        InstGroup instGroup = instGroupDao.selectOne(wrapper);
        int rec;
        if(userTService.getUserByUserId(group.getCreatorId())==null) return -2;
        if(instGroup==null){
            rec = instGroupDao.insert(group);
        }
        else rec = -1;
        return rec;
    }

    @Override
    public int update(InstGroup group) {
        QueryWrapper<InstGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("group_name",group.getGroupName());
        if(instGroupDao.selectOne(wrapper)!=null) return 2;

        InstGroup instGroup = instGroupDao.selectById(group.getId());
        int rec;
        if(instGroup==null) rec = -1;
        else{
            instGroup.setGroupName(group.getGroupName());
            rec = instGroupDao.updateById(instGroup);
        }
        return rec;
    }

    @Override
    public int delete(Integer id) {
        InstGroup instGroup = instGroupDao.selectById(id);
        int rec  = 1;
        if(instGroup==null) return -1;
        QueryWrapper<InstToGroup> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("group_id",id);
        rec *= instToGroupDao.delete(wrapper1);

        QueryWrapper<GroupToGroup> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("inst_group_id",id);
        rec *= groupToGroupDao.delete(wrapper2);

        rec *= instGroupDao.deleteById(id);
        return rec;
    }

    @Override
    public List<InstGroup> getGroupByCondition(InstGroup group) {
        QueryWrapper<InstGroup> wrapper = new QueryWrapper<>();
        if(group.getGroupName()!=null)
            wrapper.like("group_name",group.getGroupName());
        List<InstGroup> list = instGroupDao.selectList(wrapper);

        return list;
    }

    @Override
    public SelectResult getAllGroup(int current, int size) {
        current = current<=0?1:current;
        size = size<=0?4:size;
        Page<InstGroup> page = new Page<>(current,size);
        QueryWrapper<InstGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        instGroupDao.selectPage(page,queryWrapper);
        List<InstGroup> list = page.getRecords();
        return new SelectResult(page.getTotal(),list);
    }

    @Override
    public List<InstGroup> getGroupByInstId(Instrument instrument) {
        List<InstGroup> list=new ArrayList<>();
        if(instrument.getInstrumentId()==null || instrument.getInstrumentId().length()==0){
            list = instGroupDao.selectList(null);
        }
        else {
            QueryWrapper<Instrument> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("instrument_id",instrument.getInstrumentId());
            if(instrumentDao.selectOne(wrapper1)!=null){
                Instrument temp = instrumentDao.selectOne(wrapper1);
                QueryWrapper<InstToGroup> wrapper2 = new QueryWrapper<>();
                wrapper2.eq("inst_id",temp.getId());
                List<InstToGroup> l = instToGroupDao.selectList(wrapper2);
                if(l!=null&&l.size()>0){
                    for(InstToGroup t:l){
                        list.add(instGroupDao.selectById(t.getGroupId()));
                    }
                }
            }
        }
        return list;
    }
}
