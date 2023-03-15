package cn.rmy.service;

import cn.rmy.common.beans.InstrumentModel;

import java.util.List;

public interface InstrumentModelService {
    int insert(InstrumentModel instrumentModel);
    List<String> getAll();
    List<InstrumentModel> getAllModel();
    int delete(String model);
    int updateModel(InstrumentModel instrumentModel);
    boolean checkModel(String model);

}
