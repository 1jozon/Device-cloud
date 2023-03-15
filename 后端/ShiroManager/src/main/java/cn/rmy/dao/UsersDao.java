package cn.rmy.dao;


import cn.rmy.common.beans.shiroUsers.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UsersDao extends BaseMapper<UserInfo> {
}
