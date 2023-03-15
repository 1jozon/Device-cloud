package cn.rmy.service;

import cn.rmy.common.beans.gps.InsMsgVODto;


public interface InsMsgVOService {

    /**
     * 仪器信息
     *
     * @param insId ins id
     * @return {@link InsMsgVODto}
     */
    InsMsgVODto getMsgByInsId(String insId);
}
