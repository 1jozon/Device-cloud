package cn.rmy.dao;


import cn.rmy.common.beans.shiroUsers.RoleInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleDao extends BaseMapper<RoleInfo> {

    @Select("select a.* from tb_role as a " +
            "group by role_name ")
    List<RoleInfo> getAllRoleName();
}
