package cn.rmy.otherFunc;

import cn.rmy.common.beans.shiroUsers.UserInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelToList {

    public static List<UserInfo> excelToUserInfo(String resource, String fileName){
        String excelPath = resource + fileName;
        List<UserInfo> list = new ArrayList<>();

        try{
            File excel = new File(excelPath);
            //判断文件是否存在
            if (excel.exists() && excel.isFile()){
                String[] split = excel.getName().split("\\.");
                Workbook wb;
                //根据文件后缀名（xls/xlsx）进行判断，创建文件流对象
                if ("xls".equals(split[1])){
                    FileInputStream fis = new FileInputStream(excel);
                    wb = new HSSFWorkbook(fis);
                }else if ("xlsx".equals(split[1])){
                    FileInputStream fis = new FileInputStream(excel);
                    wb = new HSSFWorkbook(fis);
                }else{
                    System.out.println("文件类型错误");
                    return null;
                }

                //开始解析
                //读取sheet 0
                Sheet sheet = wb.getSheetAt(0);
                System.out.println("开始读取文件");
                int firstRowIndex = sheet.getFirstRowNum() + 1; //第一行列名，不读
                int lastRowIndex = sheet.getLastRowNum();

                System.out.println("firstRoeIndex：" + firstRowIndex);
                System.out.println("lastRoeIndex：" + lastRowIndex);
                Map<String, Integer> map = new HashMap<>();
                map.put("访客",0);
                map.put("售后",1);
                map.put("销售",2);
                map.put("研发",3);
                map.put("测试",4);
                map.put("质量",5);
                map.put("管理员",6);
                map.put("系统管理员",7);
                DecimalFormat df = new DecimalFormat("0");
                //遍历行
                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++){
                    System.out.println("rIndex" + rIndex);
                    Row row = sheet.getRow(rIndex);
                    if (row != null){
                        UserInfo userInfo = new UserInfo();
                        Cell cell = row.getCell(1);
                        String userId = df.format(cell.getNumericCellValue());
                        userInfo.setUserId(userId);
                        userInfo.setRoleId(map.get(row.getCell(0).toString()));
                        userInfo.setUserName(row.getCell(2).toString());

                        cell = row.getCell(3);
                        String userPhone = df.format(cell.getNumericCellValue());
                        userInfo.setUserPhone(userPhone);
                        cell = row.getCell(5);
                        String userEmail = df.format(cell.getNumericCellValue());
                        userInfo.setUserEmail(userEmail);
                        userInfo.setUserPassword(userId);
                        userInfo.setRegistStatus(1);
                        list.add(userInfo);
                    }
                }
            }else{
                System.out.println("找不到指定文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
