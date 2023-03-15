package cn.rmy.service;

import cn.rmy.common.pojo.dto.CaliMsgDTO;
import cn.rmy.common.beans.analysis.CaliDataInfo;

import java.util.List;

/**
 * 定标点数据处理服务
 *
 * @author chu
 * @date 2021/11/16
 */
public interface CaliPointDataService {

    /**
     * 初始化
     *
     * @return {@link CaliMsgDTO}
     */
    CaliMsgDTO initCaliMsg();

    /**
     * 装换定标数据info
     *
     * @param caliMsgDTO 卡利味精dto
     * @return {@link CaliDataInfo}
     */
    CaliDataInfo getCaliInfo(CaliMsgDTO caliMsgDTO);

    /**
     * 插入新数据
     *
     * @param caliMsgDTO 卡利味精dto
     */
    void insertNewData(CaliMsgDTO caliMsgDTO);

    /**
     * 得到完整的信息
     *
     * @param caliMsgCache cali2
     * @param caliMsg cali2
     * @return {@link CaliDataInfo}
     */
    CaliMsgDTO getWholeInfo(CaliMsgDTO caliMsgCache, CaliMsgDTO caliMsg);

    /**
     * 多条件查询
     *
     * @param InsIdList ins id列表
     * @return {@link List}<{@link CaliDataInfo}>
     */
    List<CaliDataInfo> getTestInfoListOnUser(List<String> InsIdList);

}
