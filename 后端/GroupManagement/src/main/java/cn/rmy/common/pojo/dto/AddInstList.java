package cn.rmy.common.pojo.dto;

import cn.rmy.common.beans.groupManager.InstToGroup;
import lombok.Data;

import java.util.List;

@Data
public class AddInstList {
    List<InstToGroup> list;
}
