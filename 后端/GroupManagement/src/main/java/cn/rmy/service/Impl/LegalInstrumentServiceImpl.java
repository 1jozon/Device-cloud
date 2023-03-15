package cn.rmy.service.Impl;

import cn.rmy.common.beans.LegalInstrument;
import cn.rmy.dao.LegalInstrumentDao;
import cn.rmy.service.LegalInstrumentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LegalInstrumentServiceImpl implements LegalInstrumentService {

    @Autowired
    LegalInstrumentDao legalInstrumentDao;

    @Override
    public int insertList(List<LegalInstrument> list) {
        for(LegalInstrument l : list){
            if(l.getInstrumentId()!=null && l.getInstrumentModel()!=null){
                LegalInstrument l1 = new LegalInstrument();
                l1.setInstrumentId(l.getInstrumentId())
                        .setInstrumentModel(l.getInstrumentModel());
                int rec = legalInstrumentDao.insert(l1);
                if(rec<1) return rec;
            }
        }
        return 1;
    }

    @Override
    public List<LegalInstrument> getByCondition(LegalInstrument l) {
        QueryWrapper<LegalInstrument> queryWrapper = new QueryWrapper<>();
        if(l.getInstrumentId()!=null)
            queryWrapper.like("instrument_id",l.getInstrumentId());
        if(l.getInstrumentModel()!=null)
            queryWrapper.like("instrument_model",l.getInstrumentModel());

        List<LegalInstrument> list = legalInstrumentDao.selectList(queryWrapper);
        if(list==null)
            return null;
        else{
            List<LegalInstrument> res = new ArrayList<>();
            //返回结果的前10个
            for(int i=0;i<Math.min(list.size(),10);i++){
                res.add(list.get(i));
            }
            return res;
        }
    }

    @Override
    public LegalInstrument getByInstrumentId(String instrumentId) {
        QueryWrapper<LegalInstrument> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("instrument_id",instrumentId);
        LegalInstrument l = legalInstrumentDao.selectOne(queryWrapper);

        //未作null判断
        return l;
    }

    @Override
    public int delByInstrumentId(String instrumentId) {
        QueryWrapper<LegalInstrument> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("instrument_id",instrumentId);
        //LegalInstrument l = legalInstrumentDao.selectOne(queryWrapper);
        int rec = legalInstrumentDao.delete(queryWrapper);
        return rec;
    }
}
