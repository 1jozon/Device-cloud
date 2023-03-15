package cn.rmy.dto;

import cn.rmy.common.beans.faultManagement.FaultCode;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 一句话功能描述.
 * 项目名称:
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:
 * 创建时间:
 */
@Data
@Accessors(chain = true)
public class FaultCodeReq {
    private FaultCode faultCode;
    private String oldFaultCode;
}
