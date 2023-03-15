package cn.rmy.common.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstGroupVO {

    private int id;

    private String groupName;

    //加一个groupId
    private int groupId;

    private String instrumentId;

    private String instrumentModel;

}
