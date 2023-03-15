package cn.rmy.dao;

import cn.rmy.common.beans.groupManager.UserGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserGroupDao extends BaseMapper<UserGroup> {
}
