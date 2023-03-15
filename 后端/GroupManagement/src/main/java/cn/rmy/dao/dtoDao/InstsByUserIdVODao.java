package cn.rmy.dao.dtoDao;

import cn.rmy.common.beans.Instrument;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InstsByUserIdVODao extends BaseMapper<Instrument> {


    @Select("select distinct a.* " +
            "from tb_instrument AS a, tb_user AS b, user_to_group AS c, group_to_group AS d, instrument_to_group AS e " +
            "where b.user_id=#{userId} " +
            "AND b.id=c.us_id " +
            "AND d.us_group_id=c.group_id " +
            "AND e.group_id=d.inst_group_id " +
            "AND a.id=e.inst_id " +
            "AND a.deleted=0 AND b.deleted=0 AND c.deleted=0 AND d.deleted=0 AND e.deleted=0 ")
    List<Instrument> getInstsByUserId(String userId);
}
