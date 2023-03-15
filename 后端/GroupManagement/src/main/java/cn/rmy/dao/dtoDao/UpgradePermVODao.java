package cn.rmy.dao.dtoDao;

import cn.rmy.common.beans.UpgradePermVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UpgradePermVODao extends BaseMapper<UpgradePermVO> {

    @Select("select c.id AS package_id,a.instrument_id AS inst_id " +
            "from tb_instrument AS a, instrument_models AS b, upgrade_package AS c " +
            "where a.instrument_id=#{instrumentId} " +
            "AND a.instrument_model=b.instrument_model " +
            "AND b.id=c.model_id " +
            "AND a.deleted=0 AND b.deleted=0 AND c.deleted=0 ")
    List<UpgradePermVO> getUpgradePerms(@Param("instrumentId") String instrumentId);
}
