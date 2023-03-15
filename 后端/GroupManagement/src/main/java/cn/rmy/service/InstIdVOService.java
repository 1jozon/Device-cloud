package cn.rmy.service;

import cn.rmy.common.pojo.dto.InstCondition;
import cn.rmy.common.pojo.dto.InstIdVO;

import java.util.List;

public interface InstIdVOService {

    public List<InstIdVO> getInstIdVO(InstCondition instCondition);

}
