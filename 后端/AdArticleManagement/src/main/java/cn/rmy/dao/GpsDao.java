package cn.rmy.dao;

import cn.rmy.common.pojo.dto.InsMsgDto;
import cn.rmy.common.beans.articleGps.GpsInfo;
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
public interface GpsDao extends BaseMapper<GpsInfo> {
    /**
     * 获取仪器信息
     *
     * @param insId ins id
     * @return {@link InsMsgDto}
     */
    @Select("select b.lac as lac, b.cid as cid " +
            "from ins_gps as b " +
            "where b.instrument_id = #{insId} ")
    InsMsgDto getMsgByIns(@Param("insId") String insId);

}
