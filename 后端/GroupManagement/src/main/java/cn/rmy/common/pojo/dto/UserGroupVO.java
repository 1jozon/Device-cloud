package cn.rmy.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserGroupVO {

    private int id;

    private String groupName;
    //加一个groupId
    private int groupId;

    private String userId;

    private String userName;
}
