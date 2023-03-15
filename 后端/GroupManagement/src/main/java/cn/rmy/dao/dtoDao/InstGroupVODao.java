package cn.rmy.dao.dtoDao;


import cn.rmy.common.pojo.dto.InstGroupVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InstGroupVODao extends BaseMapper<InstGroupVO> {

    @Select("select a.id,b.group_name,a.group_id,c.instrument_id,c.instrument_model " +
            "from instrument_to_group AS a,instrument_group AS b,tb_instrument AS c " +
            "where a.group_id=#{groupId} " +
            "AND b.id=#{groupId} " +
            "AND c.id=a.inst_id " +
            "AND a.deleted=0")
    List<InstGroupVO> getInstByGroupId(Integer groupId);
}
