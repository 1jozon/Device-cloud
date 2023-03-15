package cn.rmy.dao;

import cn.rmy.common.beans.articleGps.GpsInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * 全球定位系统(gps)映射器
 *
 * @author chu
 * @date 2021/11/12
 */
@Mapper
public interface GpsMapper extends BaseMapper<GpsInfo> {

    /**
     * 得到最新的ins地址
     *
     * @param insId ins id
     * @return {@link List}<{@link String}>
     */
    List<GpsInfo> getNewestInsAddresses(String insId);

    /**
     * 上线时间搜索地址
     *
     * @param startOT 开始不
     * @param endOT   结束不
     * @return {@link List}<{@link GpsInfo}>
     */
    List<GpsInfo> getOnlineTimeAddresses(Date startOT,Date endOT);
}
