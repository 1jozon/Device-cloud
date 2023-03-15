package cn.rmy.dao;


import cn.rmy.common.beans.shiroUsers.PermissionInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionDao extends BaseMapper<PermissionInfo> {
}
