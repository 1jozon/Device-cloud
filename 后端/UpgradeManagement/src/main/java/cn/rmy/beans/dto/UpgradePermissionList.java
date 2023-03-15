package cn.rmy.beans.dto;

import cn.rmy.beans.UpgradePermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpgradePermissionList {
    private List<UpgradePermission> list;
}
