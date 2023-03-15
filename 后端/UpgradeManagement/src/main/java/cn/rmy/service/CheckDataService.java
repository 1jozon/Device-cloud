package cn.rmy.service;

import cn.rmy.beans.SelectResult;
import cn.rmy.beans.dto.CheckDataDto;
import cn.rmy.common.beans.checkData.CheckData;

import java.util.Date;
import java.util.List;

public interface CheckDataService {
    void insert(List<CheckData> list);

    void insertBatch(List<CheckData> list);

    int update(CheckData checkData);

    int delete(CheckData checkData);

    SelectResult getByCondition(CheckDataDto checkDataDto, int current, int size, String startTime,String endTime,String userId);
    SelectResult getByCondition(CheckDataDto checkDataDto, int current, int size, String startTime,String endTime);
    List<CheckDataDto> getByCondition(CheckDataDto checkDataDto, String startTime,String endTime,String userId);
    List<CheckDataDto> getByCondition(CheckDataDto checkDataDto, String startTime,String endTime);
}
