package cn.rmy.service;

import cn.rmy.common.beans.analysis.ConsumablesDataInfo;
import cn.rmy.common.dto.ConsumablesDataDto;

import java.util.List;

/**
 * 可消费的服务
 *
 * @author chu
 * @date 2021/12/25
 */
public interface ConsumableService {

    /**
     * 插入数据
     *
     * @param consumablesData 耗材数据
     * @return int
     */
    int insertData(ConsumablesDataDto consumablesData);

    /**
     * 得到固体状态
     *
     * @return int
     */
    int getSolidStatus(String unit);

    /**
     * 得到单位
     *
     * @param volUsed 使用卷
     * @return {@link String}
     */
    String getUnit(String volUsed);

    /**
     * 得到使用量
     *
     * @param volUsed 使用卷
     * @return {@link Float}
     */
    Float getVol(String volUsed);

    /**
     * 获取数据信息
     *
     * @param data 数据
     * @return {@link ConsumablesDataInfo}
     */
    ConsumablesDataInfo getDataInfo(ConsumablesDataDto data);

    /**
     * 得到所有耗材类型
     *
     * @return {@link List}<{@link String}>
     */
    List<String> getAllConsumType();
}
