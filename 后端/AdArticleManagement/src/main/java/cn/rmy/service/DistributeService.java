package cn.rmy.service;

import cn.rmy.common.beans.Instrument;
import cn.rmy.common.pojo.dto.City;
import cn.rmy.common.pojo.dto.Province;

import java.util.List;
import java.util.Map;

/**
 * 仪器分布服务
 *
 * @author chu
 * @date 2021/12/01
 */
public interface DistributeService {

    /**
     * 获得省份分布
     *
     * @return {@link List}<{@link Province}>
     */
    List<Province> getProvinceDistribute();

    /**
     * 获得城市分布
     *
     * @param province 省
     * @return {@link List}<{@link City}>
     */
    List<City> getCityDistribute(String province);

    /**
     * 拆分地址
     *
     * @param address 地址
     * @return {@link Map}<{@link String}, {@link String}>
     */
    Map<String, String>splitAddress(String address);

    /**
     * 获得所有仪器
     *
     * @return {@link List}<{@link Instrument}>
     */
    List<Instrument> getAllInstruments();
}
