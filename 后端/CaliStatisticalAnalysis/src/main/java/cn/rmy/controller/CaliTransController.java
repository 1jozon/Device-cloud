package cn.rmy.controller;

import cn.rmy.common.pojo.dto.CaliMsgDTO;
import cn.rmy.redisUtils.RedisUtils;
import cn.rmy.service.Imp.CaliPointDataServiceImp;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * 定标数据控制
 *
 * @author chu
 * @date 2022/02/21
 */
@Slf4j
@Component
//@RabbitListener(queues = "rmy.queue.$share/g1/CaliData")
@RabbitListener(queues = "rmy.queue.$share/g2/CaliData")
public class CaliTransController {

    private final static Logger logger = LoggerFactory.getLogger(CaliTransController.class);

    @Autowired
    private CaliPointDataServiceImp caliPointDataService;

    @Autowired
    RedisUtils redisUtils;

    @RabbitHandler
    //@LogAnno(operateType = "获取定标数据")
    public void CaliProcess(String message) throws Exception{

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject object = JSONObject.parseObject(message);
        CaliMsgDTO caliMsg = caliPointDataService.initCaliMsg();

        if(object.get("CaliPoint1") != null){
            //接收第一段数据
            String caliPoint1 = ((String)object.get("CaliPoint1")).equals("") ? "CA00000000001" : (String) object.get("CaliPoint1");
            String caliPoint2 = ((String)object.get("CaliPoint2")).equals("") ? "CA00000000002" : (String) object.get("CaliPoint2");
            caliMsg.setProjectId((String) object.get("ProjectID"));
            caliMsg.setReagentBatchId((String) object.get("ReagentBatchID"));
            caliMsg.setCaliTime(format.parse((String) object.get("CaliTime")));
            caliMsg.setEffectiveTime(format.parse((String) object.get("EffectiveTime")));
            caliMsg.setMeasureType(Integer.valueOf(object.get("MeasureType").toString()));
            caliMsg.setCutoff(Double.valueOf(object.get("Cutoff").toString()));
            caliMsg.setSampleType((String) (object.get("sampleType")));

            caliMsg.setCaliPoint1(caliPoint1);
            caliMsg.setCaliPoint1Repeat1(Integer.valueOf( object.get("CaliPoint1Repeat1").toString()));
            caliMsg.setCaliPoint1Repeat2(Integer.valueOf( object.get("CaliPoint1Repeat2").toString()));
            caliMsg.setCaliPoint1Repeat3(Integer.valueOf( object.get("CaliPoint1Repeat3").toString()));
            caliMsg.setCaliPoint1Average(Integer.valueOf( object.get("CaliPoint1Average").toString()));
            caliMsg.setCaliPoint1CV(Double.valueOf( object.get("CaliPoint1CV").toString()));
            caliMsg.setCaliPoint1DEV(Double.valueOf( object.get("CaliPoint1DEV").toString()));
            caliMsg.setCaliRotio1(Double.valueOf( object.get("CaliRatio1").toString()));
            caliMsg.setCardConc1(Double.valueOf( object.get("CardConc1").toString()));
            caliMsg.setCaliOffset1(Double.valueOf( object.get("CaliOffset1").toString()));
            caliMsg.setCardRLU1(Integer.valueOf( object.get("CardRLU1").toString()));

            caliMsg.setCaliPoint2(caliPoint2);
            caliMsg.setCaliPoint2Repeat1(Integer.valueOf( object.get("CaliPoint2Repeat1").toString()));
            caliMsg.setCaliPoint2Repeat2(Integer.valueOf( object.get("CaliPoint2Repeat2").toString()));
            caliMsg.setCaliPoint2Repeat3(Integer.valueOf( object.get("CaliPoint2Repeat3").toString()));
            caliMsg.setCaliPoint2Average(Integer.valueOf( object.get("CaliPoint2Average").toString()));
            caliMsg.setCaliPoint2CV(Double.valueOf( object.get("CaliPoint2CV").toString()));
            caliMsg.setCaliPoint2DEV(Double.valueOf( object.get("CaliPoint2DEV").toString()));
            caliMsg.setCaliRotio2(Double.valueOf( object.get("CaliRatio2").toString()));
            caliMsg.setCardConc2(Double.valueOf( object.get("CardConc2").toString()));
            caliMsg.setCaliOffset2(Double.valueOf( object.get("CaliOffset2").toString()));
            caliMsg.setCardRLU2(Integer.valueOf( object.get("CardRLU2").toString()));

        }else{
            //接受第二段数据
            String caliPoint3 = ((String)object.get("CaliPoint3")).equals("") ? "CA00000000003" : (String) object.get("CaliPoint3");
            String caliPoint4 = ((String)object.get("CaliPoint4")).equals("") ? "CA00000000004" : (String) object.get("CaliPoint4");
            String caliPoint5 = ((String)object.get("CaliPoint5")).equals("") ? "CA00000000005" : (String) object.get("CaliPoint5");
            String caliPoint6 = ((String)object.get("CaliPoint6")).equals("") ? "CA00000000006" : (String) object.get("CaliPoint6");

            caliMsg.setCaliPoint3(caliPoint3);
            caliMsg.setCaliPoint3Repeat1(Integer.valueOf( object.get("CaliPoint3Repeat1").toString()));
            caliMsg.setCaliPoint3Repeat2(Integer.valueOf( object.get("CaliPoint3Repeat2").toString()));
            caliMsg.setCaliPoint3Repeat3(Integer.valueOf( object.get("CaliPoint3Repeat3").toString()));
            caliMsg.setCaliPoint3Average(Integer.valueOf( object.get("CaliPoint3Average").toString()));
            caliMsg.setCaliPoint3CV(Double.valueOf( object.get("CaliPoint3CV").toString()));
            caliMsg.setCaliPoint3DEV(Double.valueOf( object.get("CaliPoint3DEV").toString()));

            caliMsg.setCaliPoint4(caliPoint4);
            caliMsg.setCaliPoint4Repeat1(Integer.valueOf( object.get("CaliPoint4Repeat1").toString()));
            caliMsg.setCaliPoint4Repeat2(Integer.valueOf( object.get("CaliPoint4Repeat2").toString()));
            caliMsg.setCaliPoint4Repeat3(Integer.valueOf( object.get("CaliPoint4Repeat3").toString()));
            caliMsg.setCaliPoint4Average(Integer.valueOf( object.get("CaliPoint4Average").toString()));
            caliMsg.setCaliPoint4CV(Double.valueOf( object.get("CaliPoint4CV").toString()));
            caliMsg.setCaliPoint4DEV(Double.valueOf( object.get("CaliPoint4DEV").toString()));

            caliMsg.setCaliPoint5(caliPoint5);
            caliMsg.setCaliPoint5Repeat1(Integer.valueOf( object.get("CaliPoint5Repeat1").toString()));
            caliMsg.setCaliPoint5Repeat2(Integer.valueOf( object.get("CaliPoint5Repeat2").toString()));
            caliMsg.setCaliPoint5Repeat3(Integer.valueOf( object.get("CaliPoint5Repeat3").toString()));
            caliMsg.setCaliPoint5Average(Integer.valueOf( object.get("CaliPoint5Average").toString()));
            caliMsg.setCaliPoint5CV(Double.valueOf( object.get("CaliPoint5CV").toString()));
            caliMsg.setCaliPoint5DEV(Double.valueOf( object.get("CaliPoint5DEV").toString()));

            caliMsg.setCaliPoint6(caliPoint6);
            caliMsg.setCaliPoint6Repeat1(Integer.valueOf( object.get("CaliPoint6Repeat1").toString()));
            caliMsg.setCaliPoint6Repeat2(Integer.valueOf( object.get("CaliPoint6Repeat2").toString()));
            caliMsg.setCaliPoint6Repeat3(Integer.valueOf( object.get("CaliPoint6Repeat3").toString()));
            caliMsg.setCaliPoint6Average(Integer.valueOf( object.get("CaliPoint6Average").toString()));
            caliMsg.setCaliPoint6CV(Double.valueOf( object.get("CaliPoint6CV").toString()));
            caliMsg.setCaliPoint6DEV(Double.valueOf( object.get("CaliPoint6DEV").toString()));

            caliMsg.setCaliRotio3(Double.valueOf( object.get("CaliRatio3").toString()));
            caliMsg.setCaliRotio4(Double.valueOf( object.get("CaliRatio4").toString()));
            caliMsg.setCaliRotio5(Double.valueOf( object.get("CaliRatio5").toString()));

            caliMsg.setCaliOffset3(Double.valueOf( object.get("CaliOffset3").toString()));
            caliMsg.setCaliOffset4(Double.valueOf( object.get("CaliOffset4").toString()));
            caliMsg.setCaliOffset5(Double.valueOf( object.get("CaliOffset5").toString()));

            caliMsg.setCardConc3(Double.valueOf( object.get("CardConc3").toString()));
            caliMsg.setCardConc4(Double.valueOf( object.get("CardConc4").toString()));
            caliMsg.setCardConc5(Double.valueOf( object.get("CardConc5").toString()));
            caliMsg.setCardConc6(Double.valueOf( object.get("CardConc6").toString()));

            caliMsg.setCardRLU3(Integer.valueOf( object.get("CardRLU3").toString()));
            caliMsg.setCardRLU4(Integer.valueOf( object.get("CardRLU4").toString()));
            caliMsg.setCardRLU5(Integer.valueOf( object.get("CardRLU5").toString()));
            caliMsg.setCardRLU6(Integer.valueOf( object.get("CardRLU6").toString()));
        }
        caliMsg.setInsId((String) object.get("ID"));
        caliMsg.setCaliId((String) object.get("CaliID"));

        boolean hasKey = redisUtils.exists("cali:" + caliMsg.getCaliId());
        if(hasKey){
            //获取缓存
            CaliMsgDTO caliMsgOnePiece = (CaliMsgDTO)redisUtils.get("cali:"+ caliMsg.getCaliId());

            //防止同一段重复接收
            String firstPieceFlag = "CA00000000001";
            CaliMsgDTO info = new CaliMsgDTO();

            if(caliMsgOnePiece.getCaliPoint1() .equals(firstPieceFlag) && (!caliMsg.getCaliPoint1().equals(firstPieceFlag))
            || (!caliMsgOnePiece.getCaliPoint1().equals(firstPieceFlag)) && caliMsg.getCaliPoint1().equals(firstPieceFlag)){
                info = caliPointDataService.getWholeInfo(caliMsgOnePiece, caliMsg);

            }else{
                logger.info("遇到重复数据");
                return;
            }

            try{
                caliPointDataService.insertNewData(info);
                redisUtils.del("cali:" + caliMsg.getCaliId());
            }catch (Exception e){
                e.printStackTrace();
                logger.info("缓存定标存储异常");
            }
        }else{
            //存入缓存和数据库
            log.info("定标数据存入缓存");
            redisUtils.set("cali:"+caliMsg.getCaliId(), caliMsg, 3 * 60 * 60);
            //指定操作库
            //redisUtils.setOnDB("cali"+caliMsg.getCaliId(), caliMsg, 1, true,100L);
        }
    }
}
