package cn.rmy.common.ResultEnums;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public enum CommonResultEm {
    // 通用enum：
    SUCCESS         ("200","成功"),
    ERROR           ("400","失败"),
    BIND_ERROR      ("111101","参数校验异常"),
    ALREADY_EXIST   ("111102","已存在"),
    NOT_EXIST       ("111103","不存在"),

    // 用户登录enum：900xx
    // 管理员...：800xx
    // 订单：700xx
    NUMBER_IS_NOT_ENOUGH    ("70001","库存不足");

    private String ecode;

    private String emsg;

    public String getEcode() {
        return ecode;
    }

    public String getEmsg() {
        return emsg;
    }

    public static CommonResultEm statOf(String ecode) {
        for (CommonResultEm state : values())
            if (state.getEcode().equals(ecode))
                return state;
        return null;
    }
}
