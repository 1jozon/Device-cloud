package cn.rmy.service;


import cn.rmy.common.beans.InstDetails;
import cn.rmy.common.beans.Instrument;
import cn.rmy.common.pojo.dto.InstCondition;
import cn.rmy.common.pojo.dto.SelectResult;

public interface InstrumentService {
    int insert(Instrument instrument);
    int insertTestInstruemnt(Instrument instrument);
    SelectResult getByCondition(Instrument instrument, int current, int size);
    int update(Instrument instrument);
    int delete(Integer id);
    Instrument getOneByInstrumentId(String instrumentId);
    int setOnlineStatus(String instrumentId,int onlineStatus);
    int setFaultStatus(String instrumentId,int faultStatus);
    SelectResult getInstByCondition(InstCondition instCondition, int current, int size);
    SelectResult getInstByCondition(InstCondition instCondition);

    InstDetails getInstDetails(String instrumentId);

    int setUpgradePermission(String instrumentId);
}
