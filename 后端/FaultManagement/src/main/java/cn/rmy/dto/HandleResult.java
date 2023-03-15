package cn.rmy.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 一句话功能描述.
 * 项目名称:  处理结果
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:
 * 创建时间:
 */
@Data
@Accessors(chain = true)
public class HandleResult {
    private int notHandleNum;

    private int hasHandleNum;
}
