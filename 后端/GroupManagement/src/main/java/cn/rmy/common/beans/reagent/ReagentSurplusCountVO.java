package cn.rmy.common.beans.reagent;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 一句话功能描述.
 * 项目名称:  试剂余量
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:
 * 创建时间:
 */
@Data
@TableName("tb_reagentSurplusCount")
@Accessors(chain = true)
public class ReagentSurplusCountVO implements Serializable {

    //  每天固定时间、换盒，盒用完 按开机自查上次未发送的，均要发送  （？？有疑问 不懂什么意思）
    //  按--天--统计余量

    //    @IsMobile
    @TableId(type = IdType.AUTO,value = "reagent_surplus_count_id")
    private int reagentSurplusCountId;

    private String deviceId;

    // 最大30个字符
    private String reagentBoxId;

    // 最大30个字符
    private String reagentNum;

    // 最大30个字符
    private String reagentPorj;


    // 试剂余量
//    private int reagentSurplus;

    // 试剂使用量
    private int reagentUseNum;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date getTime;

    @TableField(fill = FieldFill.INSERT)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Version
    private Integer version;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
