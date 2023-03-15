package cn.rmy.common.ResultEnums;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public enum CommonResultEm {
    // 通用enum：
    SUCCESS         ("200","成功"),
    ERROR           ("400","失败，用户发出的请求异常"),
    BIND_ERROR      ("111101","参数校验异常"),
    ALREADY_EXIST   ("111102","已存在"),
    NOT_EXIST       ("111103","不存在"),
    REQ_PARAM_IS_ERROR   ("111104","入参错误"),
    UPDATE_ERROR    ("111105","更新失败"),
    ERROR_PARMAS_USERID_NOT_EXIST    ("111106","请求参数错误，userId参数不存在"),

    GREATED         ("201", "新建或修改数据成功"),
    NOT_CONTENET    ("204", "删除数据成功"),
    Unauthoried     ("401", "用户未进行认证"),
    Forbidden       ("403", "用户禁止访问"),
    NOT_FOUND       ("404", "用户请求针对的是不存在的记录"),
    NOT_Acceptable  ("406", "用户请求格式异常"),
    INTERNAL_SERVER_ERROR ("500", "服务器内部错误"),
    Service_Unavailable ("503","服务器不可用");

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
