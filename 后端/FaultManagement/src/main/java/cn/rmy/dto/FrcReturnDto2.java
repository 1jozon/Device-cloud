package cn.rmy.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

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
public class FrcReturnDto2 {
    private String faultCode;

    private List<FrcDto> frcDtoList;
}
