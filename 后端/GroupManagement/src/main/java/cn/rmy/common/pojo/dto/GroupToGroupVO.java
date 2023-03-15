package cn.rmy.common.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
