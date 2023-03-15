package cn.rmy.service.imp;

import cn.rmy.common.dto.Users;
import cn.rmy.common.beans.Instrument;
import cn.rmy.common.beans.groupManager.UserT;
import cn.rmy.common.pojo.dto.emaildto.MailDto;
import cn.rmy.common.beans.articleGps.GpsInfo;
import cn.rmy.emailUtil.SendMailUtil;
import cn.rmy.dao.GpsDao;
import cn.rmy.service.GpsService;
import cn.rmy.service.Impl.InstrumentServiceImpl;
import cn.rmy.service.Impl.UserWithInstServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * gps服务
 *
 * @author chu
 * @date 2021/11/12
 */
@Service
@Transactional
public class GpsServiceImp implements GpsService {

    /*@Autowired
    private JavaMailSender mailSender;*/

    @Autowired
    private GpsDao gpsDao;

    @Autowired
    private InstrumentServiceImpl instrumentService;

    @Autowired
    private UserWithInstServiceImpl userWithInstService;

    @Autowired
    private UsersServiceImp usersServiceImp;


    /**
     * 存储信息
     *
     * @param gpsinfo gpsinfo
     * @return int
     */
    @Override
    public int intoSql(GpsInfo gpsinfo) {
        String insId = gpsinfo.getInstrumentId();
        Instrument ins = instrumentService.getOneByInstrumentId(insId);
        if (ins == null){
            //注册测试仪器
            Instrument newIns = new Instrument();
            newIns.setInstrumentId(gpsinfo.getInstrumentId());
            newIns.setInstrumentModel(gpsinfo.getInstrumentMode());
            int recRegistIns = instrumentService.insertTestInstruemnt(newIns);
        }

        QueryWrapper<GpsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("instrument_id", insId);
        GpsInfo info = gpsDao.selectOne(queryWrapper);

        if (info == null){
            int rec = gpsDao.insert(gpsinfo);
            //1=插入成功 -1=插入失败
            return rec==1 ? 1 : -1;
        }else {
            //仪器gps信息已经获取,判断串货
            if((!(info.getCid().equals(gpsinfo.getCid()))) || (!info.getLac().equals(gpsinfo.getLac()))){
                //串货,向维护人员发送邮件
                collusionNotice(gpsinfo, info, ins.getInstrumentMaintainerId());
                info.setCid(gpsinfo.getCid());
                info.setLac(gpsinfo.getLac());
                gpsDao.updateById(info);
            }
            return 2;
        }
    }

    /**
     * 串货判断
     *
     * @return int
     */
    @Override
    public GpsInfo collusion(GpsInfo gpsInfo) {
        if (gpsInfo == null){
            //地址信息为空
            return null;
        }
        String insId = gpsInfo.getInstrumentId();
        String newLac = gpsInfo.getLac();
        String newCid = gpsInfo.getCid();
        QueryWrapper<GpsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("instrument_id", insId);
        GpsInfo info = gpsDao.selectOne(queryWrapper);
        if (info == null){
            return null;
        }

        if ((!info.getCid().equals(newCid)) || !(info.getLac().equals(newLac))){
            //地址变化，串货
            return info;
        }else{
            return null;
        }
    }

    /**
     * 串货通知
     *
     * @param gpsInfo gps信息
     * @param userId  用户id
     * @param oldInfo 旧的信息
     * @return int
     */
    @Override
    public int collusionNotice(GpsInfo gpsInfo, GpsInfo oldInfo,String userId) {

        //串货
        MailDto mail = new MailDto();
        mail.setSubject("串货通知");
        String content = "尊敬的"+userId+"您好：\n"
                + "您所负责的"+gpsInfo.getInstrumentMode()+"仪器，仪器号："+gpsInfo.getInstrumentId()
                + "，出现串货，由注册地址lac："+oldInfo.getLac()+"，cid："+oldInfo.getCid()
                +"，出现在地址lac："+gpsInfo.getLac()+"，cid："+gpsInfo.getCid()+"。请您及时处理。";
        mail.setContent(content);

        //收件人
        List<UserT> list = userWithInstService.getUsersByInstId(gpsInfo.getInstrumentId());
        if (list == null){
            return -1;
        }
        int i = 0;
        String[] emails = new String[list.size()];
        for (UserT user : list){
            Users users = usersServiceImp.getUserById(user.getUserId());
            if (users == null){
                continue;
            }
            String email = users.getUserEmail();
            if (email == null){
                continue;
            }else{
                emails[i++] = email;
            }
        }
        mail.setTos(emails);

        //发送邮件
        SendMailUtil.send(mail);

        return 1;
    }
}
