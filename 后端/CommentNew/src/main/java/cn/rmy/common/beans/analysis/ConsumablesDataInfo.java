package cn.rmy.common.beans.analysis;

import com.alibaba.druid.support.ibatis.SpringIbatisBeanNameAutoProxyCreator;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 耗材数据信息
 *
 * @author chu
 * @date 2021/12/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("consumables_data")
public class ConsumablesDataInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    private String insId;

    private String conName;

    private int solidState;

    private Float volUsed;

    private String unit;

    /**
     * 更换耗材时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 测试时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dateTime;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Version
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer version;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;


}
