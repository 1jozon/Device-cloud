package cn.rmy.service.Impl;

import cn.rmy.common.beans.Instrument;
import cn.rmy.common.beans.InstrumentModel;
import cn.rmy.dao.InstrumentDao;
import cn.rmy.dao.InstrumentModelDao;
import cn.rmy.service.InstrumentModelVOService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class InstrumentModelVOServiceImpl implements InstrumentModelVOService {

    @Autowired
    private InstrumentDao instrumentDao;

    @Autowired
    private InstrumentModelDao instrumentModelDao;

    @Override
    public int getModelId(String instrumentId) {
        QueryWrapper<Instrument> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("instrument_id",instrumentId);
        Instrument instrument = instrumentDao.selectOne(queryWrapper);
        if(instrument==null) return -1;

        QueryWrapper<InstrumentModel> wrapper = new QueryWrapper<>();
        wrapper.eq("instrument_model",instrument.getInstrumentModel());
        InstrumentModel instrumentModel = instrumentModelDao.selectOne(wrapper);
        if(instrumentModel==null) return -1;

        return instrumentModel.getId();
    }

    @Override
    public Map<Integer, String> getTypeName() {
        List<InstrumentModel> list = instrumentModelDao.selectList(null);
        Map<Integer,String> map = new HashMap<>();
        for(InstrumentModel temp:list){
            map.put(temp.getId(),temp.getInstrumentModel());
        }
        return map;
    }
}
