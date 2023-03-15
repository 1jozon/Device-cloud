package cn.rmy.utils;

import cn.rmy.beans.dto.CheckDataDto;
import cn.rmy.common.beans.faultManagement.FaultCode;
import cn.rmy.common.beans.faultManagement.FaultRecord;
import cn.rmy.common.beans.faultManagement.Log;
import cn.rmy.common.redisUtils.DateCommonUtil;
import com.csvreader.CsvWriter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckDataListToExcel {

   // @Value("${totalRowNumber}")
    private final static int totalRowNumber = 50000;

    //Goods对象写入excel
    public static void checkDataToExcel(String resource, String fileName, List<CheckDataDto> list, Map<Integer,String> map) throws IOException {
        //exportFilePath：D:/测试
        //定义表头
        String[] title = {"仪器编号","仪器类型","患者年龄","患者性别","患者姓名","患者地区","项目名称","测试LRU值","测试结果","参考范围","数据单位","稀释倍数","试剂批号","样本类型","异常情况","检查时间"};
        //创建excel工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表sheet
        XSSFSheet sheet = workbook.createSheet();
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = null;
        //插入第一行数据的表头
        for(int i = 0; i < title.length; i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }

        //写入数据
        if(list!=null){
            for (int i = 0; i < list.size() && i < totalRowNumber; i++){
                XSSFRow nrow = sheet.createRow(i+1);
                XSSFCell ncell = nrow.createCell(0);
                ncell.setCellValue(list.get(i).getInstrumentId());
                ncell = nrow.createCell(1);
                ncell.setCellValue(map.getOrDefault(list.get(i).getModelId(),""));
                ncell = nrow.createCell(2);
                ncell.setCellValue(list.get(i).getPatientAge());
                ncell = nrow.createCell(3);
                ncell.setCellValue(list.get(i).getPatientSex()==1?"男":"女");
                ncell = nrow.createCell(4);
                ncell.setCellValue(list.get(i).getPatientName());
//                ncell = nrow.createCell(5);
//                ncell.setCellValue(faultRecordList.get(i).getFaultType());
                ncell = nrow.createCell(5);
                ncell.setCellValue(list.get(i).getPatientArea());
                ncell = nrow.createCell(6);
                ncell.setCellValue(list.get(i).getProjectName());
                ncell = nrow.createCell(7);
                ncell.setCellValue(list.get(i).getTestRlu());
                ncell = nrow.createCell(8);
                ncell.setCellValue(list.get(i).getTestResult());
                ncell = nrow.createCell(9);
                ncell.setCellValue(list.get(i).getReferenceRange());
                ncell = nrow.createCell(10);
                ncell.setCellValue(list.get(i).getUnit());
                ncell = nrow.createCell(11);
                ncell.setCellValue(list.get(i).getDiluRatio());
                ncell = nrow.createCell(12);
                ncell.setCellValue(list.get(i).getReagentBatchId());
                ncell = nrow.createCell(13);
                ncell.setCellValue(list.get(i).getSampleType());
                ncell = nrow.createCell(14);
                ncell.setCellValue(list.get(i).getException());
                ncell = nrow.createCell(15);
                ncell.setCellValue(DateCommonUtil.getFormatDateStr(list.get(i).getCheckTime(),"yyyy-MM-dd HH:mm:ss"));
            }
        }

        //创建excel文件

        File file = new File(resource,fileName);

        try {
            file.createNewFile();
            //将excel写入
            FileOutputStream stream= new FileOutputStream(file);
            workbook.write(stream);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void faultRecordsToCsv(String resource, String fileName,List<FaultRecord> faultRecordList) throws IOException {

        try {
            File file = new File(resource,fileName);
            file.createNewFile();
            //将excel写入
            FileOutputStream stream= new FileOutputStream(file);
            // 创建CSV写对象
            CsvWriter csvWriter = new CsvWriter(stream,',', Charset.forName("UTF-8"));

            // 写表头
            String[] headers = {"仪器编号","仪器类型","故障码","故障描述","故障处理建议","模块号","故障级别","出故障时间","处理状态"};
            csvWriter.writeRecord(headers);
            //写入数据
            if(faultRecordList!=null){
                for (FaultRecord faultRecord:faultRecordList) {
                    List<String> list = new ArrayList<>();
                    list.add(faultRecord.getDeviceId());
                    list.add(faultRecord.getDeviceType());
                    list.add(faultRecord.getFaultCode());
                    list.add(faultRecord.getFaultDescribe());
                    list.add(faultRecord.getFaultAdvice());
//                    list.add(faultRecord.getFaultType());
                    list.add(faultRecord.getModuleCode());
                    list.add(faultRecord.getFaultClass());
                    list.add(faultRecord.getFaultTime().toString());
                    list.add(faultRecord.getHandleStatus());
                    csvWriter.writeRecord(list.toArray(new String[list.size()]));
                }
            }
            csvWriter.close();
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static void faultCodeToExcel(String resource, String fileName,List<FaultCode> faultCodeList) throws IOException {
        //exportFilePath：D:/测试
        //定义表头
        String[] title = {"故障码","故障描述","故障处理建议"};
        //创建excel工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表sheet
        XSSFSheet sheet = workbook.createSheet();
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = null;
        //插入第一行数据的表头
        for(int i = 0; i < title.length; i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }

        //写入数据
        if(faultCodeList!=null){
            for (int i = 0; i < faultCodeList.size(); i++){
                XSSFRow nrow = sheet.createRow(i+1);
                XSSFCell ncell = nrow.createCell(0);
                ncell.setCellValue(faultCodeList.get(i).getFaultCode());
                ncell = nrow.createCell(1);
                ncell.setCellValue(faultCodeList.get(i).getFaultDescribe());
                ncell = nrow.createCell(2);
                ncell.setCellValue(faultCodeList.get(i).getFaultAdvice());
//                ncell = nrow.createCell(3);
//                ncell.setCellValue(faultCodeList.get(i).getFaultType());
            }
        }

        //创建excel文件

        File file = new File(resource,fileName);


        try {
            file.createNewFile();
            //将excel写入
            FileOutputStream stream= new FileOutputStream(file);
            workbook.write(stream);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void faultCodeToCsv(String resource, String fileName,List<FaultCode> faultCodeList) throws IOException {

        try {
            File file = new File(resource,fileName);
            file.createNewFile();
            //将excel写入
            FileOutputStream stream= new FileOutputStream(file);
            // 创建CSV写对象
            CsvWriter csvWriter = new CsvWriter(stream,',', Charset.forName("UTF-8"));

            // 写表头
            String[] headers = {"故障码","故障描述","故障处理建议"};
            csvWriter.writeRecord(headers);
            //写入数据
            if(faultCodeList!=null){
                for (FaultCode faultCode:faultCodeList) {
                    List<String> list = new ArrayList<>();
                    list.add(faultCode.getFaultCode());
                    list.add(faultCode.getFaultDescribe());
                    list.add(faultCode.getFaultAdvice());
//                    list.add(faultCode.getFaultType());
                    csvWriter.writeRecord(list.toArray(new String[list.size()]));
                }
            }
            csvWriter.close();
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void logToExcel(String resource, String fileName,List<Log> logList) throws IOException {
        //exportFilePath：D:/测试
        //定义表头
        String[] title = {"操作人员","操作详情","操作时间"};
        //创建excel工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表sheet
        XSSFSheet sheet = workbook.createSheet();
        //创建第一行
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = null;
        //插入第一行数据的表头
        for(int i = 0; i < title.length; i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
        }

        //写入数据
        if(logList!=null){
            for (int i = 0; i < logList.size(); i++){
                XSSFRow nrow = sheet.createRow(i+1);
                XSSFCell ncell = nrow.createCell(0);
                ncell.setCellValue(logList.get(i).getOperateor());
                ncell = nrow.createCell(1);
                ncell.setCellValue(logList.get(i).getOperateType());
                ncell = nrow.createCell(2);
                ncell.setCellValue(DateCommonUtil.getFormatDateStr(logList.get(i).getOperateDate(),"yyyy-MM-dd HH:mm:ss"));
//                ncell = nrow.createCell(3);
//                ncell.setCellValue(faultCodeList.get(i).getFaultType());
            }
        }

        //创建excel文件

        File file = new File(resource,fileName);


        try {
            file.createNewFile();
            //将excel写入
            FileOutputStream stream= new FileOutputStream(file);
            workbook.write(stream);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void logToCsv(String resource, String fileName,List<Log> logList) throws IOException {

        try {
            File file = new File(resource,fileName);
            file.createNewFile();
            //将excel写入
            FileOutputStream stream= new FileOutputStream(file);
            // 创建CSV写对象
            CsvWriter csvWriter = new CsvWriter(stream,',', Charset.forName("UTF-8"));

            // 写表头
            String[] headers = {"操作人员","操作详情","操作时间"};
            csvWriter.writeRecord(headers);
            //写入数据
            if(logList!=null){
                for (Log log:logList) {
                    List<String> list = new ArrayList<>();
                    list.add(log.getOperateor());
                    list.add(log.getOperateType());
                    list.add(DateCommonUtil.getFormatDateStr(log.getOperateDate(),"yyyy-MM-dd HH:mm:ss"));
//                    list.add(faultCode.getFaultType());
                    csvWriter.writeRecord(list.toArray(new String[list.size()]));
                }
            }
            csvWriter.close();
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
