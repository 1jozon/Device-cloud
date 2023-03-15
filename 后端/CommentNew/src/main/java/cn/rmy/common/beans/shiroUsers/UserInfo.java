package cn.rmy.common.beans.shiroUsers;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@TableName("tb_user")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserInfo {


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
