package cn.rmy.service;

import cn.rmy.common.beans.LegalInstrument;

import java.util.List;

public interface LegalInstrumentService {
    int insertList(List<LegalInstrument> list);
    List<LegalInstrument> getByCondition(LegalInstrument legalInstrument);
    LegalInstrument getByInstrumentId(String instrumentId);
    int delByInstrumentId(String instrumentId);
}
