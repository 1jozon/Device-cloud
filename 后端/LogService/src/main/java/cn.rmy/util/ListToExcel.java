package cn.rmy.util;

import cn.rmy.common.beans.faultManagement.FaultCode;
import cn.rmy.common.beans.faultManagement.FaultRecord;
import cn.rmy.common.beans.faultManagement.Log;
import cn.rmy.common.redisUtils.DateCommonUtil;
import com.csvreader.CsvWriter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ListToExcel {
    //Goods对象写入excel
    public static void faultRecordsToExcel(String resource, String fileName,List<FaultRecord> faultRecordList) throws IOException {
        //exportFilePath：D:/测试
        //定义表头
        String[] title = {"仪器编号","仪器类型","故障码","故障描述","故障处理建议","模块号","故障级别","出故障时间","处理状态"};
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
        if(faultRecordList!=null){
            for (int i = 0; i < faultRecordList.size(); i++){
                XSSFRow nrow = sheet.createRow(i+1);
                XSSFCell ncell = nrow.createCell(0);
                ncell.setCellValue(faultRecordList.get(i).getDeviceId());
                ncell = nrow.createCell(1);
                ncell.setCellValue(faultRecordList.get(i).getDeviceType());
                ncell = nrow.createCell(2);
                ncell.setCellValue(faultRecordList.get(i).getFaultCode());
                ncell = nrow.createCell(3);
                ncell.setCellValue(faultRecordList.get(i).getFaultDescribe());
                ncell = nrow.createCell(4);
                ncell.setCellValue(faultRecordList.get(i).getFaultAdvice());
//                ncell = nrow.createCell(5);
//                ncell.setCellValue(faultRecordList.get(i).getFaultType());
                ncell = nrow.createCell(5);
                ncell.setCellValue(faultRecordList.get(i).getModuleCode());
                ncell = nrow.createCell(6);
                ncell.setCellValue(faultRecordList.get(i).getFaultClass());
                ncell = nrow.createCell(7);
                ncell.setCellValue(DateCommonUtil.getFormatDateStr(faultRecordList.get(i).getFaultTime(),"yyyy-MM-dd HH:mm:ss"));
                ncell = nrow.createCell(8);
                ncell.setCellValue(faultRecordList.get(i).getHandleStatus());
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
