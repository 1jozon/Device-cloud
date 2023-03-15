package cn.rmy.service.Impl;

import cn.rmy.common.pojo.dto.InstCondition;
import cn.rmy.common.pojo.dto.InstIdVO;
import cn.rmy.dao.dtoDao.InstIdVODao;
import cn.rmy.service.InstIdVOService;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InstIdVOServiceImpl implements InstIdVOService {

    @Autowired
    private InstIdVODao instIdVODao;

    @Override
    public List<InstIdVO> getInstIdVO(InstCondition instCondition) {
        String instrumentId;
        if(instCondition.getInstrumentId()!=null)
            instrumentId = "%"+instCondition.getInstrumentId()+"%";
        else instrumentId = "%";
        List<InstIdVO> list = instIdVODao.getInstIdVO(instrumentId);
        return list;
    }

}
