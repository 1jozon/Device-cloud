package cn.rmy.service.Impl;

import cn.rmy.common.beans.Instrument;
import cn.rmy.common.beans.groupManager.UserT;
import cn.rmy.dao.InstrumentDao;
import cn.rmy.dao.UserTDao;
import cn.rmy.dao.dtoDao.InstsByUserIdVODao;
import cn.rmy.dao.dtoDao.UsersByInstIdVODao;
import cn.rmy.service.UserWithInstService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserWithInstServiceImpl implements UserWithInstService {

    @Autowired
    private UsersByInstIdVODao usersByInstIdVODao;

    @Autowired
    private InstrumentDao instrumentDao;

    @Autowired
    private UserTDao userTDao;

    @Autowired
    private InstsByUserIdVODao instsByUserIdVODao;


    @Override
    public List<UserT> getUsersByInstId(String instrumentId) {
        QueryWrapper<Instrument> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("instrument_id",instrumentId);
        Instrument instrument = instrumentDao.selectOne(queryWrapper);
        if(instrument==null) return null;
        UserT userT = new UserT();
        userT.setId(0);
        //userT.setUserId("维护人员");
        userT.setUserId(instrument.getInstrumentMaintainerId());
        userT.setUserName(instrument.getInstrumentMaintainerName());
        userT.setUserGender(2);
        userT.setUserPhone(instrument.getInstrumentMaintainerPhone());
        userT.setUserEmail(null);
        List<UserT> list = usersByInstIdVODao.getUsersByInstId(instrumentId);
        list.add(0, userT);
        return list;
    }

    @Override
    public List<Instrument> getInstsByUserId(String userId) {
        QueryWrapper<UserT> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        if(userTDao.selectOne(queryWrapper)==null) return null;

        List<Instrument> list = instsByUserIdVODao.getInstsByUserId(userId);
        return list;
    }
}
