package cn.rmy.common.pojo.dto;

import lombok.Data;

import java.util.Date;

@Data
public class InstCondition {

    /*
    * 传递Instrument的查询条件，比Instrument多出groupId UserId
    * */

    private int id;

    private String instrumentId;

    private String instrumentModel;

    private String instrumentAddress;

    private Date instrumentDate;

    //在线状态，默认断开，取值{-1：断开，1：在线}
    private int onlineStatus;

    //在线状态，默认正常，取值{-1：故障，1：正常}
    private int faultStatus;

    private String instrumentInstallerId;

    private String instrumentInstallerName;

    private String instrumentMaintainerId;

    private String instrumentMaintainerName;

    private String instrumentMaintainerPhone;

    private String hospitalName;

    //组id
    private int groupId;

    //用户编号，对应userId，不是对应id
    private String userId;
}
