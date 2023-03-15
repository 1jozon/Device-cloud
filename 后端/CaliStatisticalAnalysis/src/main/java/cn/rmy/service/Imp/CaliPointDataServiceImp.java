package cn.rmy.service.Imp;

import cn.rmy.common.pojo.dto.CaliMsgDTO;
import cn.rmy.common.beans.analysis.CaliDataInfo;
import cn.rmy.dao.CaliDataDao;
import cn.rmy.service.CaliPointDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 定标点数据处理服务
 *
 * @author chu
 * @date 2021/11/16
 */
@Service
public class CaliPointDataServiceImp implements CaliPointDataService {


    @Autowired
    private CaliDataDao caliDataDao;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 初始化
     *
     * @return {@link CaliMsgDTO}
     */
    @Override
    public CaliMsgDTO initCaliMsg() {
        CaliMsgDTO caliMsg = new CaliMsgDTO();
        caliMsg.setInsId("B000000000001");
        caliMsg.setCaliId("R0000000000000000000000001");
        caliMsg.setProjectId("000");
        caliMsg.setReagentBatchId( "R00000000000");
        caliMsg.setCaliPoint1( "CA00000000001");
        caliMsg.setCaliPoint2( "CA00000000002");
        caliMsg.setCaliPoint3( "CA00000000003");
        caliMsg.setCaliPoint4( "CA00000000004");
        caliMsg.setCaliPoint5( "CA00000000005");
        caliMsg.setCaliPoint6( "CA00000000006");

        caliMsg.setCaliPoint1Repeat1(0);
        caliMsg.setCaliPoint1Repeat2(0);
        caliMsg.setCaliPoint1Repeat3(0);
        caliMsg.setCaliPoint1Average(0);
        caliMsg.setCaliPoint1CV(0.0);
        caliMsg.setCaliPoint1DEV(0.0);

        caliMsg.setCaliPoint2Repeat1(0);
        caliMsg.setCaliPoint2Repeat2(0);
        caliMsg.setCaliPoint2Repeat3(0);
        caliMsg.setCaliPoint2Average(0);
        caliMsg.setCaliPoint2CV(0.0);
        caliMsg.setCaliPoint2DEV(0.0);

        caliMsg.setCaliPoint3Repeat1(0);
        caliMsg.setCaliPoint3Repeat2(0);
        caliMsg.setCaliPoint3Repeat3(0);
        caliMsg.setCaliPoint3Average(0);
        caliMsg.setCaliPoint3CV(0.0);
        caliMsg.setCaliPoint3DEV(0.0);

        caliMsg.setCaliPoint4Repeat1(0);
        caliMsg.setCaliPoint4Repeat2(0);
        caliMsg.setCaliPoint4Repeat3(0);
        caliMsg.setCaliPoint4Average(0);
        caliMsg.setCaliPoint4CV(0.0);
        caliMsg.setCaliPoint4DEV(0.0);

        caliMsg.setCaliPoint5Repeat1(0);
        caliMsg.setCaliPoint5Repeat2(0);
        caliMsg.setCaliPoint5Repeat3(0);
        caliMsg.setCaliPoint5Average(0);
        caliMsg.setCaliPoint5CV(0.0);
        caliMsg.setCaliPoint5DEV(0.0);

        caliMsg.setCaliPoint6Repeat1(0);
        caliMsg.setCaliPoint6Repeat2(0);
        caliMsg.setCaliPoint6Repeat3(0);
        caliMsg.setCaliPoint6Average(0);
        caliMsg.setCaliPoint6CV(0.0);
        caliMsg.setCaliPoint6DEV(0.0);


        caliMsg.setCaliTime(new Date());
        caliMsg.setEffectiveTime(new Date());

        caliMsg.setCaliRotio1(0.0);
        caliMsg.setCaliRotio2(0.0);
        caliMsg.setCaliRotio3(0.0);
        caliMsg.setCaliRotio4(0.0);
        caliMsg.setCaliRotio5(0.0);
        caliMsg.setCaliOffset1(0.0);
        caliMsg.setCaliOffset2(0.0);
        caliMsg.setCaliOffset3(0.0);
        caliMsg.setCaliOffset4(0.0);
        caliMsg.setCaliOffset5(0.0);

        caliMsg.setCardConc1(0.0);
        caliMsg.setCardConc2(0.0);
        caliMsg.setCardConc3(0.0);
        caliMsg.setCardConc4(0.0);
        caliMsg.setCardConc5(0.0);
        caliMsg.setCardConc6(0.0);
        caliMsg.setCardRLU1(0);
        caliMsg.setCardRLU2(0);
        caliMsg.setCardRLU3(0);
        caliMsg.setCardRLU4(0);
        caliMsg.setCardRLU5(0);
        caliMsg.setCardRLU6(0);

        caliMsg.setMeasureType(1);
        caliMsg.setCutoff(0.0);
        caliMsg.setSampleType("默认数据");

        return caliMsg;
    }

    /**
     * 装换定标数据info-y
     *
     * @param caliMsgDTO 卡利味精dto
     * @return {@link CaliDataInfo}
     */
    @Override
    public CaliDataInfo getCaliInfo(CaliMsgDTO caliMsgDTO) {
        CaliDataInfo caliDataInfo = new CaliDataInfo();
        if (caliMsgDTO != null &&
                caliMsgDTO.getInsId().length() != 0 &&
                caliMsgDTO.getReagentBatchId().length() != 0){
            caliDataInfo.setInsId(caliMsgDTO.getInsId());
            caliDataInfo.setCaliId(caliMsgDTO.getCaliId());
            caliDataInfo.setProjectId(caliMsgDTO.getProjectId());
            caliDataInfo.setReagentBatchId(caliMsgDTO.getReagentBatchId());
            caliDataInfo.setCaliPointId1(caliMsgDTO.getCaliPoint1());
            caliDataInfo.setCaliPointId2(caliMsgDTO.getCaliPoint2());
            caliDataInfo.setCaliPointId3(caliMsgDTO.getCaliPoint3());
            caliDataInfo.setCaliPointId4(caliMsgDTO.getCaliPoint4());
            caliDataInfo.setCaliPointId5(caliMsgDTO.getCaliPoint5());
            caliDataInfo.setCaliPointId6(caliMsgDTO.getCaliPoint6());


            caliDataInfo.setCaliPoint1Repeat1(caliMsgDTO.getCaliPoint1Repeat1());
            caliDataInfo.setCaliPoint1Repeat2(caliMsgDTO.getCaliPoint1Repeat2());
            caliDataInfo.setCaliPoint1Repeat3(caliMsgDTO.getCaliPoint1Repeat3());
            caliDataInfo.setCaliPoint1Average(caliMsgDTO.getCaliPoint1Average());
            caliDataInfo.setCaliPoint1Cv(caliMsgDTO.getCaliPoint1CV());
            caliDataInfo.setCaliPoint1Dev(caliMsgDTO.getCaliPoint1DEV());

            caliDataInfo.setCaliPoint2Repeat1(caliMsgDTO.getCaliPoint2Repeat1());
            caliDataInfo.setCaliPoint2Repeat2(caliMsgDTO.getCaliPoint2Repeat2());
            caliDataInfo.setCaliPoint2Repeat3(caliMsgDTO.getCaliPoint2Repeat3());
            caliDataInfo.setCaliPoint2Average(caliMsgDTO.getCaliPoint2Average());
            caliDataInfo.setCaliPoint2Cv(caliMsgDTO.getCaliPoint2CV());
            caliDataInfo.setCaliPoint2Dev(caliMsgDTO.getCaliPoint2DEV());

            caliDataInfo.setCaliPoint3Repeat1(caliMsgDTO.getCaliPoint3Repeat1());
            caliDataInfo.setCaliPoint3Repeat2(caliMsgDTO.getCaliPoint3Repeat2());
            caliDataInfo.setCaliPoint3Repeat3(caliMsgDTO.getCaliPoint3Repeat3());
            caliDataInfo.setCaliPoint3Average(caliMsgDTO.getCaliPoint3Average());
            caliDataInfo.setCaliPoint3Cv(caliMsgDTO.getCaliPoint3CV());
            caliDataInfo.setCaliPoint3Dev(caliMsgDTO.getCaliPoint3DEV());

            caliDataInfo.setCaliPoint4Repeat1(caliMsgDTO.getCaliPoint4Repeat1());
            caliDataInfo.setCaliPoint4Repeat2(caliMsgDTO.getCaliPoint4Repeat2());
            caliDataInfo.setCaliPoint4Repeat3(caliMsgDTO.getCaliPoint4Repeat3());
            caliDataInfo.setCaliPoint4Average(caliMsgDTO.getCaliPoint4Average());
            caliDataInfo.setCaliPoint4Cv(caliMsgDTO.getCaliPoint4CV());
            caliDataInfo.setCaliPoint4Dev(caliMsgDTO.getCaliPoint4DEV());

            caliDataInfo.setCaliPoint5Repeat1(caliMsgDTO.getCaliPoint5Repeat1());
            caliDataInfo.setCaliPoint5Repeat2(caliMsgDTO.getCaliPoint5Repeat2());
            caliDataInfo.setCaliPoint5Repeat3(caliMsgDTO.getCaliPoint5Repeat3());
            caliDataInfo.setCaliPoint5Average(caliMsgDTO.getCaliPoint5Average());
            caliDataInfo.setCaliPoint5Cv(caliMsgDTO.getCaliPoint5CV());
            caliDataInfo.setCaliPoint5Dev(caliMsgDTO.getCaliPoint5DEV());

            caliDataInfo.setCaliPoint6Repeat1(caliMsgDTO.getCaliPoint6Repeat1());
            caliDataInfo.setCaliPoint6Repeat2(caliMsgDTO.getCaliPoint6Repeat2());
            caliDataInfo.setCaliPoint6Repeat3(caliMsgDTO.getCaliPoint6Repeat3());
            caliDataInfo.setCaliPoint6Average(caliMsgDTO.getCaliPoint6Average());
            caliDataInfo.setCaliPoint6Cv(caliMsgDTO.getCaliPoint6CV());
            caliDataInfo.setCaliPoint6Dev(caliMsgDTO.getCaliPoint6DEV());

            caliDataInfo.setCaliTime(caliMsgDTO.getCaliTime());
            caliDataInfo.setEffectiveTime(caliMsgDTO.getEffectiveTime());

            caliDataInfo.setCaliRotio1(caliMsgDTO.getCaliRotio1());
            caliDataInfo.setCaliRotio2(caliMsgDTO.getCaliRotio2());
            caliDataInfo.setCaliRotio3(caliMsgDTO.getCaliRotio3());
            caliDataInfo.setCaliRotio4(caliMsgDTO.getCaliRotio4());
            caliDataInfo.setCaliRotio5(caliMsgDTO.getCaliRotio5());

            caliDataInfo.setCaliOffset1(caliMsgDTO.getCaliOffset1());
            caliDataInfo.setCaliOffset2(caliMsgDTO.getCaliOffset2());
            caliDataInfo.setCaliOffset3(caliMsgDTO.getCaliOffset3());
            caliDataInfo.setCaliOffset4(caliMsgDTO.getCaliOffset4());
            caliDataInfo.setCaliOffset5(caliMsgDTO.getCaliOffset5());

            caliDataInfo.setCardConc1(caliMsgDTO.getCardConc1());
            caliDataInfo.setCardConc2(caliMsgDTO.getCardConc2());
            caliDataInfo.setCardConc3(caliMsgDTO.getCardConc3());
            caliDataInfo.setCardConc4(caliMsgDTO.getCardConc4());
            caliDataInfo.setCardConc5(caliMsgDTO.getCardConc5());
            caliDataInfo.setCardConc6(caliMsgDTO.getCardConc6());

            caliDataInfo.setCardRlu1(caliMsgDTO.getCardRLU1());
            caliDataInfo.setCardRlu2(caliMsgDTO.getCardRLU2());
            caliDataInfo.setCardRlu3(caliMsgDTO.getCardRLU3());
            caliDataInfo.setCardRlu4(caliMsgDTO.getCardRLU4());
            caliDataInfo.setCardRlu5(caliMsgDTO.getCardRLU5());
            caliDataInfo.setCardRlu6(caliMsgDTO.getCardRLU6());

            caliDataInfo.setMeasureType(caliMsgDTO.getMeasureType());
            caliDataInfo.setCutoff(caliMsgDTO.getCutoff());
            caliDataInfo.setSampleType(caliMsgDTO.getSampleType());

           caliDataInfo.setCreateTime(new Date());
           caliDataInfo.setUpdateTime(new Date());
           caliDataInfo.setVersion(1);
           caliDataInfo.setDeleted(0);

        }else {
            return null;
        }
        return caliDataInfo;
    }

    /**
     * 插入新数据
     * 插入新数据-y
     *
     * @param caliMsgDTO 卡利味精dto
     */
    @Override
    public void insertNewData(CaliMsgDTO caliMsgDTO) {

        CaliDataInfo caliDataInfo = getCaliInfo(caliMsgDTO);
        System.out.println(caliDataInfo);
        try {
            caliDataDao.insert(caliDataInfo);
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    /**
     * 得到完整的信息
     *
     * @param caliMsgCache cali2
     * @param caliMsg cali2
     * @return {@link CaliDataInfo}
     */
    @Override
    public CaliMsgDTO getWholeInfo(CaliMsgDTO caliMsgCache, CaliMsgDTO caliMsg) {

        if(caliMsgCache.getCaliPoint1() != null && caliMsgCache.getCaliPoint1().equals("CA00000000001")){
            //cache存在第二段,msg为第一段，更新msg
            caliMsg.setCaliPoint3(caliMsgCache.getCaliPoint3().equals("") ? "CA00000000003" : caliMsgCache.getCaliPoint3());
            caliMsg.setCaliPoint3Repeat1(caliMsgCache.getCaliPoint3Repeat1());
            caliMsg.setCaliPoint3Repeat2(caliMsgCache.getCaliPoint3Repeat2());
            caliMsg.setCaliPoint3Repeat3(caliMsgCache.getCaliPoint3Repeat3());
            caliMsg.setCaliPoint3Average(caliMsgCache.getCaliPoint3Average());
            caliMsg.setCaliPoint3CV(caliMsgCache.getCaliPoint3CV());
            caliMsg.setCaliPoint3DEV(caliMsgCache.getCaliPoint3DEV());
            caliMsg.setCardConc3(caliMsgCache.getCardConc3());
            caliMsg.setCardRLU3(caliMsgCache.getCardRLU3());
            caliMsg.setCaliRotio3(caliMsgCache.getCaliRotio3());
            caliMsg.setCaliOffset3(caliMsgCache.getCaliOffset3());

            caliMsg.setCaliPoint4(caliMsgCache.getCaliPoint4().equals("") ? "CA00000000004" : caliMsgCache.getCaliPoint4());
            caliMsg.setCaliPoint4Repeat1(caliMsgCache.getCaliPoint4Repeat1());
            caliMsg.setCaliPoint4Repeat2(caliMsgCache.getCaliPoint4Repeat2());
            caliMsg.setCaliPoint4Repeat3(caliMsgCache.getCaliPoint4Repeat3());
            caliMsg.setCaliPoint4Average(caliMsgCache.getCaliPoint4Average());
            caliMsg.setCaliPoint4CV(caliMsgCache.getCaliPoint4CV());
            caliMsg.setCaliPoint4DEV(caliMsgCache.getCaliPoint4DEV());
            caliMsg.setCardConc4(caliMsgCache.getCardConc4());
            caliMsg.setCardRLU4(caliMsgCache.getCardRLU4());
            caliMsg.setCaliRotio4(caliMsgCache.getCaliRotio4());
            caliMsg.setCaliOffset4(caliMsgCache.getCaliOffset4());

            caliMsg.setCaliPoint5(caliMsgCache.getCaliPoint5().equals("") ? "CA00000000005" : caliMsgCache.getCaliPoint5());
            caliMsg.setCaliPoint5Repeat1(caliMsgCache.getCaliPoint5Repeat1());
            caliMsg.setCaliPoint5Repeat2(caliMsgCache.getCaliPoint5Repeat2());
            caliMsg.setCaliPoint5Repeat3(caliMsgCache.getCaliPoint5Repeat3());
            caliMsg.setCaliPoint5Average(caliMsgCache.getCaliPoint5Average());
            caliMsg.setCaliPoint5CV(caliMsgCache.getCaliPoint5CV());
            caliMsg.setCaliPoint5DEV(caliMsgCache.getCaliPoint5DEV());
            caliMsg.setCardConc5(caliMsgCache.getCardConc5());
            caliMsg.setCardRLU5(caliMsgCache.getCardRLU5());
            caliMsg.setCaliRotio5(caliMsgCache.getCaliRotio5());
            caliMsg.setCaliOffset5(caliMsgCache.getCaliOffset5());

            caliMsg.setCaliPoint6(caliMsgCache.getCaliPoint6().equals("") ? "CA00000000006" : caliMsgCache.getCaliPoint6());
            caliMsg.setCaliPoint6Repeat1(caliMsgCache.getCaliPoint6Repeat1());
            caliMsg.setCaliPoint6Repeat2(caliMsgCache.getCaliPoint6Repeat2());
            caliMsg.setCaliPoint6Repeat3(caliMsgCache.getCaliPoint6Repeat3());
            caliMsg.setCaliPoint6Average(caliMsgCache.getCaliPoint6Average());
            caliMsg.setCaliPoint6CV(caliMsgCache.getCaliPoint6CV());
            caliMsg.setCaliPoint6DEV(caliMsgCache.getCaliPoint6DEV());
            caliMsg.setCardConc6(caliMsgCache.getCardConc6());
            caliMsg.setCardRLU6(caliMsgCache.getCardRLU6());

        }else{
            //cache存在第一段， msg为第二段
            caliMsg.setCaliPoint3(caliMsg.getCaliPoint3().equals("") ? "CA00000000003" : caliMsgCache.getCaliPoint6());
            caliMsg.setCaliPoint4(caliMsg.getCaliPoint4().equals("") ? "CA00000000004" : caliMsgCache.getCaliPoint6());
            caliMsg.setCaliPoint5(caliMsg.getCaliPoint5().equals("") ? "CA00000000005" : caliMsgCache.getCaliPoint6());
            caliMsg.setCaliPoint6(caliMsg.getCaliPoint6().equals("") ? "CA00000000006" : caliMsgCache.getCaliPoint6());

            caliMsg.setProjectId(caliMsgCache.getProjectId());
            caliMsg.setReagentBatchId(caliMsgCache.getReagentBatchId());
            caliMsg.setCaliTime(caliMsgCache.getCaliTime());
            caliMsg.setEffectiveTime(caliMsgCache.getEffectiveTime());
            caliMsg.setMeasureType(caliMsgCache.getMeasureType());
            caliMsg.setCutoff(caliMsgCache.getCutoff());
            caliMsg.setSampleType(caliMsgCache.getSampleType());

            caliMsg.setCaliPoint1(caliMsgCache.getCaliPoint1());
            caliMsg.setCaliPoint1Repeat1(caliMsgCache.getCaliPoint1Repeat1());
            caliMsg.setCaliPoint1Repeat2(caliMsgCache.getCaliPoint1Repeat2());
            caliMsg.setCaliPoint1Repeat3(caliMsgCache.getCaliPoint1Repeat3());
            caliMsg.setCaliPoint1Average(caliMsgCache.getCaliPoint1Average());
            caliMsg.setCaliPoint1CV(caliMsgCache.getCaliPoint1CV());
            caliMsg.setCaliPoint1DEV(caliMsgCache.getCaliPoint1DEV());
            caliMsg.setCardConc1(caliMsgCache.getCardConc1());
            caliMsg.setCardRLU1(caliMsgCache.getCardRLU1());
            caliMsg.setCaliRotio1(caliMsgCache.getCaliRotio1());
            caliMsg.setCaliOffset1(caliMsgCache.getCaliOffset1());

            caliMsg.setCaliPoint2(caliMsgCache.getCaliPoint2());
            caliMsg.setCaliPoint2Repeat1(caliMsgCache.getCaliPoint2Repeat1());
            caliMsg.setCaliPoint2Repeat2(caliMsgCache.getCaliPoint2Repeat2());
            caliMsg.setCaliPoint2Repeat3(caliMsgCache.getCaliPoint2Repeat3());
            caliMsg.setCaliPoint2Average(caliMsgCache.getCaliPoint2Average());
            caliMsg.setCaliPoint2CV(caliMsgCache.getCaliPoint2CV());
            caliMsg.setCaliPoint2DEV(caliMsgCache.getCaliPoint2DEV());
            caliMsg.setCardConc2(caliMsgCache.getCardConc2());
            caliMsg.setCardRLU2(caliMsgCache.getCardRLU2());
            caliMsg.setCaliRotio2(caliMsgCache.getCaliRotio2());
            caliMsg.setCaliOffset2(caliMsgCache.getCaliOffset2());
        }
        return caliMsg;
    }

    /**
     * 多条件查询
     *
     * @param InsIdList ins id列表
     * @return {@link List}<{@link CaliDataInfo}>
     */
    @Override
    public List<CaliDataInfo> getTestInfoListOnUser(List<String> InsIdList) {
        return null;
    }
}
