package cn.rmy.common.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * dto包下的bean都是用来传输数据的，没有与之对应的数据库表，该目录下的beans保存的是联合查询的结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupToGroupVO {

    private int id;

    private int usGroupId;

    private int instGroupId;

    private String usGroupName;

    private String instGroupName;

}
