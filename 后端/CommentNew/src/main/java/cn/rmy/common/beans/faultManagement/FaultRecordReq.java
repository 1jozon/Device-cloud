package cn.rmy.common.beans.faultManagement;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 一句话功能描述.
 * 项目名称:   故障处理信息   插入时候用到  insert
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:   xsc
 * 创建时间:
 */
@Data
@Accessors(chain = true)
public class FaultRecordReq implements Serializable {

    private String deviceId;

    private String faultCode;

    private String moduleCode;

    private String faultClass;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date faultTime;
}
