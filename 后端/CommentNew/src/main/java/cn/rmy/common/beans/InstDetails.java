package cn.rmy.common.beans;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstDetails {
    private int id;

    private String instrumentId;

    private String instrumentModel;

    private String instrumentAddress;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date instrumentDate;

    //在线状态，默认断开，取值{-1：断开，1：在线}
    private int onlineStatus;

    private String instrumentMaintainerName;

    private String hospitalName;

    //试剂用量
    private String sumReagent;

    private String location;

    private Object reagentConsume;

    private Object sampleConsume;
}
