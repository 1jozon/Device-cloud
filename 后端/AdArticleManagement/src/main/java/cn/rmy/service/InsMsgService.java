package cn.rmy.service;

import cn.rmy.common.pojo.dto.InsMsgDto;

public interface InsMsgService {

    /**
     * 仪器信息
     *
     * @param insId ins id
     * @return {@link InsMsgDto}
     */
    InsMsgDto getMsgByInsId(String insId);
}
