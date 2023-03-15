package cn.rmy.dao;


import cn.rmy.common.beans.shiroUsers.UserRoleInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleDao extends BaseMapper<UserRoleInfo> {
}
