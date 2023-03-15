package cn.rmy.controller;

import cn.rmy.common.dto.ConsumablesDataDto;
import cn.rmy.service.imp.ConsumableServiceImp;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 耗材控制器
 *
 * @author chu
 * @date 2021/12/28
 */
@Slf4j
@Component
//@RabbitListener(queues = "rmy.queue.$share/g1/consumables")
@RabbitListener(queues = "rmy.queue.$share/g2/consumables")
public class ConsumablesController {

    @Autowired
    private ConsumableServiceImp consumableService;

    @RabbitHandler
    public void consumProcess(String message) throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        JSONObject object = JSONObject.parseObject(message);
        ConsumablesDataDto consumablesData = new ConsumablesDataDto();
        String conName;
        consumablesData.setInsId((String)object.get("ID"));
        if (object.get("consumablesName") == null || ((String)object.get("consumablesName")).equals("")){
            conName = "未知耗材";
        }else{
            conName = (String) object.get("consumablesName");
        }
        consumablesData.setConName(conName);
        consumablesData.setVolUsed((String) object.get("VolUsed"));
        //consumablesData.setUpdateTime((Date) object.get("UpdateDate"));
        consumablesData.setUpdateTime(format.parse((String)object.get("UpdateDate")));
        consumablesData.setDateTime( format.parse((String)object.get("DateTime")));

        int rec = consumableService.insertData(consumablesData);
        if(rec == 1){
            log.info("耗材信息添加成功");
        } else {
            log.info("耗材信息添加失败");
        }
    }
}
