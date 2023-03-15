package cn.rmy.service;

import java.util.Map;

public interface InstrumentModelVOService {
    int getModelId(String instrumentId);
    Map<Integer,String> getTypeName();
}
