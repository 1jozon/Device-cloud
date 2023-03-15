package cn.rmy.dao.dtoDao;

import cn.rmy.common.pojo.dto.GroupToGroupVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupToGroupVODao extends BaseMapper<GroupToGroupVO> {

    @Select("select a.id,a.us_group_id,a.inst_group_id,b.group_name AS us_group_name,c.group_name AS inst_group_name " +
            "from group_to_group AS a, user_group AS b, instrument_group AS c " +
            "where a.us_group_id=#{usGroupId} " +
            "AND b.id=a.us_group_id " +
            "AND c.id=a.inst_group_id " +
            "AND a.deleted=0")
    List<GroupToGroupVO> getGTGByUsGroupId(Integer usGroupId);

    @Select("select a.id,a.us_group_id,a.inst_group_id,b.group_name AS us_group_name,c.group_name AS inst_group_name " +
            "from group_to_group AS a, user_group AS b, instrument_group AS c " +
            "where a.inst_group_id=#{instGroupId} " +
            "AND b.id=a.us_group_id " +
            "AND c.id=a.inst_group_id " +
            "AND a.deleted=0")
    List<GroupToGroupVO> getGTGByInstGroupId(Integer instGroupId);

    @Select("select a.id,a.us_group_id,a.inst_group_id,b.group_name AS us_group_name,c.group_name AS inst_group_name " +
            "from group_to_group AS a, user_group AS b, instrument_group AS c " +
            "where a.us_group_id=#{usGroupId} " +
            "AND a.inst_group_id=#{instGroupId} " +
            "AND b.id=a.us_group_id " +
            "AND c.id=a.inst_group_id " +
            "AND a.deleted=0")
    List<GroupToGroupVO> getGTGByGroupId(@Param("usGroupId") Integer usGroupId, @Param("instGroupId") Integer instGroupId);
}
