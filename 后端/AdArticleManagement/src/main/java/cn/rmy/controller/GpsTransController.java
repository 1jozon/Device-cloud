package cn.rmy.controller;

import cn.rmy.common.beans.articleGps.GpsInfo;
import cn.rmy.service.imp.GpsServiceImp;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Slf4j
@Component
//@RabbitListener(queues = "rmy.queue.$share/g1/GPSInfo")
@RabbitListener(queues = "rmy.queue.$share/g2/GPSInfo")
public class GpsTransController {

    @Autowired
    private GpsServiceImp gpsService;

    @RabbitHandler
    public void GpsProcess(String message) throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        JSONObject object = JSONObject.parseObject(message);
        GpsInfo gpsInfo = new GpsInfo();
        gpsInfo.setInstrumentId((String) object.get("ID"));
        gpsInfo.setInstrumentMode((String) object.get("Mode"));
        gpsInfo.setLac((String) object.get("LAC"));
        gpsInfo.setCid((String) object.get("CID"));
        //gpsInfo.setOnlineTime((Date) object.get("OnlineTime"));
        gpsInfo.setOnlineTime(format.parse((String)object.get("OnlineTime")));

        int rec = gpsService.intoSql(gpsInfo);
        if (rec == 1 || rec == 2){
            log.info(gpsInfo.getInstrumentId() + "位置信息获取成功");
        }else{
            log.info(gpsInfo.getInstrumentId() + "位置信息异常");
        }
    }
}
