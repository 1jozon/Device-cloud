package cn.rmy.dao.gps;


import cn.rmy.common.beans.gps.GpsVOInfo;
import cn.rmy.common.beans.gps.InsMsgVODto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * gps
 *
 * @author chu
 * @date 2021/11/12
 */
@Mapper
public interface GpsVODao extends BaseMapper<GpsVOInfo> {
    /**
     * 获取仪器信息
     *
     * @param insId ins id
     * @return
     */
    @Select("select b.lac as lac, b.cid as cid " +
            "from ins_gps as b " +
            "where b.instrument_id = #{insId} ")
    InsMsgVODto getMsgByIns(@Param("insId") String insId);

}
