package cn.rmy.service;

import cn.rmy.beans.PackageType;
import cn.rmy.beans.SelectResult;

import java.util.List;

public interface PackageTypeService {
    int insertType(PackageType packageType);
    int updateType(PackageType packageType);
    int delete(int id);
    List<PackageType> getAllType();
    SelectResult getTypeByCondition(PackageType packageType,int current,int size);
}
