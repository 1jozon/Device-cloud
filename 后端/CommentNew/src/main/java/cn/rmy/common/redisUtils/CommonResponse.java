/**
 * All rights Reserved, Designed By MiGu
 * Copyright:    Copyright(C) 2016-2020
 * Company       MiGu  Co., Ltd.
*/

package cn.rmy.common.redisUtils;


import lombok.Data;
import lombok.ToString;

/**
 * 公共的返回处理类.
 * 项目名称:
 * 包:        com.migu.colorcommon.entity
 * 类名称:    IResultCode.java
 * 类描述:    公共的返回处理类
 * 创建人:    nirui
 * 创建时间:     2017/7/27 10:47
 */
@ToString
@Data
public class CommonResponse {
    /**
     * 共同的结果.
     */
    private CommonResult result;

}

