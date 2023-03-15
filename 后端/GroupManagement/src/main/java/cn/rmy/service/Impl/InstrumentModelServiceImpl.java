package cn.rmy.service.Impl;

import cn.rmy.common.beans.InstrumentModel;
import cn.rmy.dao.InstrumentModelDao;
import cn.rmy.service.InstrumentModelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class InstrumentModelServiceImpl implements InstrumentModelService {

    @Autowired
    private InstrumentModelDao instrumentModelDao;

    @Override
    public int insert(InstrumentModel instrumentModel) {
        QueryWrapper<InstrumentModel> queryWrapper = new QueryWrapper();
        queryWrapper.eq("instrument_model",instrumentModel.getInstrumentModel());
        int rec;
        if(instrumentModelDao.selectOne(queryWrapper)==null){
            rec = instrumentModelDao.insert(instrumentModel);
        }
        else{
            rec = -1;//这个型号已经存在了
        }
        return rec;
    }

    @Override
    public List<String> getAll() {
        List<String> list = instrumentModelDao.getAllModels();
        return list;
    }

    @Override
    public List<InstrumentModel> getAllModel() {
        List<InstrumentModel> list;
        list = instrumentModelDao.selectList(null);

        return list;
    }

    @Override
    public int delete(String model) {

        QueryWrapper<InstrumentModel> queryWrapper = new QueryWrapper();
        queryWrapper.eq("instrument_model",model);
        int rec;
        if(instrumentModelDao.selectOne(queryWrapper)==null){
            rec = -1;//型号不存在
        }
        else{
            InstrumentModel instrumentModel = instrumentModelDao.selectOne(queryWrapper);
            rec = instrumentModelDao.deleteById(instrumentModel.getId());
        }
        return rec;
    }

    @Override
    public int updateModel(InstrumentModel instrumentModel) {
        int rec=0;
        if(instrumentModelDao.selectById(instrumentModel.getId())==null) rec=-1;
        else {
            InstrumentModel im = instrumentModelDao.selectById(instrumentModel.getId());
            im.setInstrumentModel(instrumentModel.getInstrumentModel());
            rec = instrumentModelDao.updateById(im);
        }
        return rec;
    }

    @Override
    public boolean checkModel(String model) {
        QueryWrapper<InstrumentModel> queryWrapper = new QueryWrapper();
        queryWrapper.eq("instrument_model",model);
        if(instrumentModelDao.selectOne(queryWrapper)==null){
            return false;//型号不存在
        }
        else
            return true;
    }
}
