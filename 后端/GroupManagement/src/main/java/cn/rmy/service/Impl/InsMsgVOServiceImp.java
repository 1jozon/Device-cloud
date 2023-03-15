package cn.rmy.service.Impl;


import cn.rmy.common.beans.gps.InsMsgVODto;
import cn.rmy.dao.gps.GpsVODao;
import cn.rmy.service.InsMsgVOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ins味精服务小鬼
 *
 * @author chu
 * @date 2021/12/27
 */
@Service
public class InsMsgVOServiceImp implements InsMsgVOService {

    @Autowired
    private GpsVODao gpsDao;

    /**
     * 仪器信息
     *
     * @param insId ins id
     * @return {@link InsMsgVODto}
     */
    @Override
    public InsMsgVODto getMsgByInsId(String insId) {
        if (insId == null || insId.length() == 0){
            return null;
        }
        InsMsgVODto insMsg = gpsDao.getMsgByIns(insId);
        return insMsg;
    }
}
