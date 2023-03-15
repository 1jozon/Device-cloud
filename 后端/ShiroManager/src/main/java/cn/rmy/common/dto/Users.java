package cn.rmy.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users implements Serializable {
    private int id;

    private String userId;

    private String userName;

    private int userGender;

    private String userPassword;

    private String userPhone;

    private String userEmail;

    private int currentRoleId;

    private int registStatus;

    private String userSalt;

    private String userAvatar;

    private String lastLoginIp;

    private Date createTime;

    //对应角色集合
    private Set<Role> roles;

    //对应角色名称集合
    Map<Integer,String> roleNames;

    //对应分组名集合
    List<String> groupNames;

}
