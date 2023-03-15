package cn.rmy.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectConditionDto {

    int all;

    String userId;

    String userName;

    String roleName;

    String userPhone;

    int roleId;

    int registStatus;
}
