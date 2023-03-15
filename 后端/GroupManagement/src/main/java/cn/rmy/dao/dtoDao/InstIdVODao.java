package cn.rmy.dao.dtoDao;

import cn.rmy.common.beans.Instrument;
import cn.rmy.common.pojo.dto.InstIdVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InstIdVODao extends BaseMapper<InstIdVO> {
    @Select("select a.id, a.instrument_id,a.instrument_model from tb_instrument AS a " +
            "where a.instrument_id like #{instrumentId} AND a.deleted=0 limit 10")
    List<InstIdVO> getInstIdVO(@Param("instrumentId") String instrumentId);

}
