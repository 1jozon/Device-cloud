package cn.rmy.dao;

import cn.rmy.beans.UpgradePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UpgradePermissionDao extends BaseMapper<UpgradePermission> {

    @Select("select a.id,a.package_id,a.inst_id,a.allowed,a.upgraded,a.create_time,a.update_time,a.version,a.deleted " +
            "from upgrade_permission AS a, upgrade_package AS b, tb_instrument AS c, instrument_models AS d " +
            "where a.package_id=#{packId} " +
            "AND b.id=#{packId} " +
            "AND a.inst_id=c.instrument_id " +
            "AND b.model_id=d.id " +
            "AND d.instrument_model=c.instrument_model " +
            "AND a.deleted=0 " +
            "AND b.deleted=0 " +
            "AND c.deleted=0")
    List<UpgradePermission> getByCondition(Integer packId);
}
