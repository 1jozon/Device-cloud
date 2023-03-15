package cn.rmy.beans.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpgradePackSendVo {

    private String packageName;

    //类型名称
    private String type;

    //型号名称
    private String model;

    private String packVersion;

    private String description;


    private String url;
    //全部禁止-0，部分授权-1，全部授权-2

}
