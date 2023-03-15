package cn.rmy.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUsersDto {
    String userId;

    //对应角色名称集合
    Map<Integer,String> roleNames;
}
