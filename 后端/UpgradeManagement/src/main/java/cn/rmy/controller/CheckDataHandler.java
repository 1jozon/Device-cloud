package cn.rmy.controller;


import cn.rmy.common.beans.checkData.CheckData;
import cn.rmy.service.CheckDataService;
import cn.rmy.service.InstrumentModelVOService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
//@RabbitListener(queues = "rmy.queue.$share/g1/checkData")
@RabbitListener(queues = "rmy.queue.$share/g2/checkData")
public class CheckDataHandler {

    @Autowired
    private InstrumentModelVOService instrumentModelService;

    @Autowired
    private CheckDataService checkDataService;

    @RabbitHandler
    public void process(String message) throws ParseException {
      //  System.out.println("检验");
        JSONObject object = JSONObject.parseObject(message);
        JSONArray jsonArray = object.getJSONArray("data");
        List<CheckData> list = new ArrayList<>();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0;i<jsonArray.size();i++){
            JSONObject temp = jsonArray.getJSONObject(i);
            CheckData checkData = new CheckData();
            checkData.setInstrumentId(temp.getString("ID"))
                    .setModelId(instrumentModelService.getModelId(temp.getString("ID")))
                    .setPatientAge(temp.getIntValue("patientAge"))
                    .setPatientSex(temp.getIntValue("patientSex")+1)
                    .setPatientName("")
                    .setPatientArea("")
                    .setProjectId(temp.getString("ProjectID"))
                    .setTestRlu(temp.getString("testRLU"))
                    .setTestResult(temp.getString("testResult"))
                    .setReferenceRange(temp.getString("referenceRange"))
                    .setUnit(temp.getString("unit"))
                    .setDiluRatio(temp.getString("diluRatio"))
                    .setReagentBatchId(temp.getString("reagentBatchID"))
                    .setSampleType(temp.getString("sampleType"))
                    .setException(temp.getString("exception"))
                    .setCheckTime(ft.parse(temp.getString("checkTime")));
            //System.out.println(ft.format(checkData.getCheckTime()));
            list.add(checkData);
        }
        //checkDataService.insert(list);
        // 批量插入
        checkDataService.insertBatch(list);
    }
}

/*测试数据
{
  "data": [
    {
      "ID": "1001",
      "patientAge": 50,
      "patientSex": 0,
      "projectName": "血蛋白",
      "testRLU": "1919199.221635",
      "testResult": "24.5",
      "referenceRange": "20-26",
      "unit": "mg/ml",
      "diluRatio": "0.6",
      "reagentBatchID": "R1010200201",
      "sampleType": "血浆",
      "exception": "正常",
      "checkTime": "2021-12-21 08:25:27"
    },
        {
      "ID": "1002",
      "patientAge": 40,
      "patientSex": 1,
      "projectName": "血蛋白",
      "testRLU": "1919199.221635",
      "testResult": "23.5",
      "referenceRange": "20-26",
      "unit": "mg/ml",
      "diluRatio": "0.6",
      "reagentBatchID": "R1010200201",
      "sampleType": "血浆",
      "exception": "正常",
      "checkTime": "2021-12-23 10:25:27"
    }

  ]
}
* */
