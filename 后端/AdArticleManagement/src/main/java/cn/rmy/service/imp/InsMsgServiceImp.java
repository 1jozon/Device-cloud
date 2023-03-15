package cn.rmy.service.imp;

import cn.rmy.common.pojo.dto.InsMsgDto;
import cn.rmy.dao.GpsDao;
import cn.rmy.service.InsMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ins味精服务小鬼
 *
 * @author chu
 * @date 2021/12/27
 */
@Service
public class InsMsgServiceImp implements InsMsgService {

    @Autowired
    private GpsDao gpsDao;

    /**
     * 仪器信息
     *
     * @param insId ins id
     * @return {@link InsMsgDto}
     */
    @Override
    public InsMsgDto getMsgByInsId(String insId) {
        if (insId == null || insId.length() == 0){
            return null;
        }
        InsMsgDto insMsg = gpsDao.getMsgByIns(insId);
        return insMsg;
    }
}
