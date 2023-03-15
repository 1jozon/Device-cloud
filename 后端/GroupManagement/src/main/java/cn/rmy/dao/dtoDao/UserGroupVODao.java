package cn.rmy.dao.dtoDao;

import cn.rmy.common.pojo.dto.UserGroupVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserGroupVODao extends BaseMapper<UserGroupVO> {

    @Select("select a.id,b.group_name,a.group_id,c.user_id,c.user_name " +
            "from user_to_group AS a,user_group AS b,tb_user AS c " +
            "where a.group_id=#{groupId} " +
            "AND b.id=#{groupId} " +
            "AND c.id=a.us_id " +
            "AND a.deleted=0")
    List<UserGroupVO> getUserByGroupId(Integer groupId);

    @Select("select b.group_name " +
            "from user_to_group AS a, user_group AS b " +
            "where a.us_id=#{usId} " +
            "AND a.group_id=b.id " +
            "AND a.deleted=0 " +
            "AND b.deleted=0")
    List<String> getGroupNameByUserId(Integer usId);
}
