package cn.rmy.beans;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("upgrade_package")
public class UpgradePackage {

    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    private String packageName;

    private int typeId;

    private int modelId;//这个属性删除，添加一个属性用来保存升级包的redis地址

    private String packVersion;

    private String description;

    private String url;

    //全部禁止-1，部分授权 1，全部授权 2
    private int authorization;


    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Version
    private Integer version;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
