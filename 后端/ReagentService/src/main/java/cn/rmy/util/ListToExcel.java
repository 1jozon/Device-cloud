//package cn.rmy.util;
//
//import cn.rmy.common.beans.faultManagement.FaultHandle;
//import cn.rmy.common.beans.faultManagement.FaultRecord;
//import cn.rmy.common.utils.DateCommonUtil;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.List;
//
//public class ListToExcel {
//    //Goods对象写入excel
//    public static void faultRecordsToExcel(String resource, String fileName,List<FaultRecord> faultRecordList) throws IOException {
//        //exportFilePath：D:/测试
//        //定义表头
//        String[] title = {"仪器编号","故障码","故障描述","故障处理建议","故障类型","模块号","故障级别","出故障时间","处理状态"};
//        //创建excel工作簿
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        //创建工作表sheet
//        XSSFSheet sheet = workbook.createSheet();
//        //创建第一行
//        XSSFRow row = sheet.createRow(0);
//        XSSFCell cell = null;
//        //插入第一行数据的表头
//        for(int i = 0; i < title.length; i++){
//            cell = row.createCell(i);
//            cell.setCellValue(title[i]);
//        }
//
//        //写入数据
//        if(faultRecordList!=null){
//            for (int i = 0; i < faultRecordList.size(); i++){
//                XSSFRow nrow = sheet.createRow(i+1);
//                XSSFCell ncell = nrow.createCell(0);
//                ncell.setCellValue(faultRecordList.get(i).getDeviceId());
//                ncell = nrow.createCell(1);
//                ncell.setCellValue(faultRecordList.get(i).getFaultCode());
//                ncell = nrow.createCell(2);
//                ncell.setCellValue(faultRecordList.get(i).getFaultDescribe());
//                ncell = nrow.createCell(3);
//                ncell.setCellValue(faultRecordList.get(i).getFaultAdvice());
//                ncell = nrow.createCell(4);
//                ncell.setCellValue(faultRecordList.get(i).getFaultType());
//                ncell = nrow.createCell(5);
//                ncell.setCellValue(faultRecordList.get(i).getModuleCode());
//                ncell = nrow.createCell(6);
//                ncell.setCellValue(faultRecordList.get(i).getFaultClass());
//                ncell = nrow.createCell(7);
//                ncell.setCellValue(DateCommonUtil.getFormatDateStr(faultRecordList.get(i).getFaultTime(),"yyyy-MM-dd HH:mm:ss"));
//                ncell = nrow.createCell(8);
//                ncell.setCellValue(faultRecordList.get(i).getHandleStatus());
//            }
//        }
//
//        //创建excel文件
//
//        File file = new File(resource,fileName);
//
//
//        try {
//            file.createNewFile();
//            //将excel写入
//            FileOutputStream stream= new FileOutputStream(file);
//            workbook.write(stream);
//            stream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public static void faultHandleToExcel(String resource, String fileName,List<FaultHandle> faultHandles) throws IOException {
//        //exportFilePath：D:/测试
//    }
//}
