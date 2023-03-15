package cn.rmy.common.beans.groupManager;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user")
@Accessors(chain = true)
public class User {
    @TableId(type = IdType.AUTO)
    private int id;

    private String userId;

    private String userName;

    private String userPassword;

    private int userGender;

    private String userPhone;

    private String userEmail;

    private int roleId;

    private int registStatus;

    private String userSalt;

    private String userAvatar;

    private String lastLoginIp;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Version
    private Integer version;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
