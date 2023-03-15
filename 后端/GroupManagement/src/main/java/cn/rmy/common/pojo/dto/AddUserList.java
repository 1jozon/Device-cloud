package cn.rmy.common.pojo.dto;

import cn.rmy.common.beans.groupManager.UserToGroup;
import lombok.Data;

import java.util.List;

@Data
public class AddUserList {
    List<UserToGroup> list;
}
