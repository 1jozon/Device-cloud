package cn.rmy.dao.dtoDao;

import cn.rmy.common.beans.groupManager.UserT;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface UsersByInstIdVODao extends BaseMapper<UserT> {

    @Select("select distinct a.id,a.user_id,a.user_name,a.user_gender,a.user_phone,a.user_email " +
            "from tb_user AS a, tb_instrument AS b, instrument_to_group AS c, group_to_group AS d, user_to_group AS e " +
            "where b.instrument_id=#{instrumentId} " +
            "AND c.inst_id=b.id " +
            "AND d.inst_group_id=c.group_id " +
            "AND e.group_id=d.us_group_id " +
            "AND a.id=e.us_id " +
            "AND a.deleted=0 AND b.deleted=0 AND c.deleted=0 AND d.deleted=0 AND e.deleted=0")
    List<UserT> getUsersByInstId(String instrumentId);
}
