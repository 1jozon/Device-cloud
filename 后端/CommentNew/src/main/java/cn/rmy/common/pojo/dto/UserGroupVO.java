package cn.rmy.common.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * dto包下的bean都是用来传输数据的，没有与之对应的数据库表，该目录下的beans保存的是联合查询的结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserGroupVO {

    private int id;

    private String groupName;

    private String userId;

    private String userName;
}
