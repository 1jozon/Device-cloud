package cn.rmy.service;

import cn.rmy.beans.UpgradePackage;
import cn.rmy.beans.UpgradePermission;

import java.util.List;

public interface UpgradePermissionService {
    int insert(UpgradePackage upgradePackage);
    List<UpgradePermission> getByPackageId(UpgradePermission upgradePermission);
    int update(List<UpgradePermission> list);
    int updateAll(UpgradePermission upgradePermission);
    int updateUpgrade(UpgradePermission upgradePermission);
    int deleteByPackageId(UpgradePermission upgradePermission);
    UpgradePermission getByCondition(String packageName,String instId);
    List<UpgradePermission> getSendInstByPackId(UpgradePackage upgradePackage);
}
