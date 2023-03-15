package cn.rmy.service;

import cn.rmy.beans.SelectResult;
import cn.rmy.beans.UpgradePackage;
import cn.rmy.beans.dto.UpgradePackSendVo;
import cn.rmy.beans.dto.UpgradePackageVO;

import java.util.List;

public interface UpgradePackageService {

    int insertPackage(UpgradePackage upgradePackage);
    int deletePackage(int id);
    SelectResult getByCondition(UpgradePackage upgradePackage, int current, int size);
    int updatePackage(UpgradePackage upgradePackage);

    UpgradePackSendVo getSendPack(UpgradePackage upgradePackage);

    int sendUpgrade(UpgradePackage upgradePackage);

    int insertPackageSyn(UpgradePackage upgradePackage) throws Exception;
}
