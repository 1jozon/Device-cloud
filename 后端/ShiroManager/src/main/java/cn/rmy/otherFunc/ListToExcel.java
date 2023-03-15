package cn.rmy.otherFunc;

import cn.rmy.common.beans.shiroUsers.UserInfo;
import cn.rmy.common.dto.Users;
import cn.rmy.common.redisUtils.DateCommonUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListToExcel {
    //User对象写入excel
    public static void userToExcel(String resource, String fileName, List<Users> users) throws IOException {
        //exportFilePath：D:/测试
        //定义表头
        String[] title = {"用户类型(访客，售后，销售，研发，测试，质量，管理员，系统管理员)","账号","姓名","手机","邮箱"};
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
        if(users!=null){
            for (int i = 0; i < users.size(); i++){
                XSSFRow nrow = sheet.createRow(i+1);
                XSSFCell ncell = nrow.createCell(0);
                Map<Integer,String> roleNames = users.get(i).getRoleNames();
                String roleName="";
                for(int k : roleNames.keySet())
                    roleName += roleNames.get(k)+",";
                ncell.setCellValue(roleName);
                ncell = nrow.createCell(1);
                ncell.setCellValue(users.get(i).getUserId());
                ncell = nrow.createCell(2);
                ncell.setCellValue(users.get(i).getUserName());
                ncell = nrow.createCell(3);
                ncell.setCellValue(users.get(i).getUserPhone());
                ncell = nrow.createCell(4);
                ncell.setCellValue(users.get(i).getUserEmail());
            }
        }

        //创建excel文件

        File file = new File(resource,fileName);


        try {
            file.createNewFile();
            //将excel写入
            FileOutputStream stream= new FileOutputStream(file);
            workbook.write(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //UserInfo对象写入excel
    public static void userInfoToExcel(String resource, String fileName, List<UserInfo> usersInfo) throws IOException {
        //exportFilePath：D:/测试
        //定义表头
        String[] title = {"用户ID","用户姓名","使用最高角色","性别","手机号","使用状态","邮箱","注册时间"};
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
        Map<Integer,String> roleNames = new HashMap<>();
        roleNames.put(1,"访客");
        roleNames.put(2,"售后");
        roleNames.put(3,"销售");
        roleNames.put(4,"研发");
        roleNames.put(5,"测试");
        roleNames.put(6,"质量");
        roleNames.put(7,"管理员");
        roleNames.put(8,"系统管理员");

        Map<Integer,String> registNames = new HashMap<>();
        registNames.put(4,"禁用");
        registNames.put(3,"审批拒绝");
        registNames.put(1,"待审批");
        registNames.put(2,"使用中");
        //写入数据
        if(usersInfo!=null){
            for (int i = 0; i < usersInfo.size(); i++){
                XSSFRow nrow = sheet.createRow(i+1);
                XSSFCell ncell = nrow.createCell(0);
                ncell.setCellValue(usersInfo.get(i).getUserId());
                ncell = nrow.createCell(1);
                ncell.setCellValue(usersInfo.get(i).getUserName());
                ncell = nrow.createCell(2);
                ncell.setCellValue(roleNames.get(usersInfo.get(i).getRoleId()));
                ncell = nrow.createCell(3);
                ncell.setCellValue(usersInfo.get(i).getUserGender()==0?"女":"男");
                ncell = nrow.createCell(4);
                ncell.setCellValue(usersInfo.get(i).getUserPhone());
                ncell = nrow.createCell(5);
                ncell.setCellValue(registNames.get(usersInfo.get(i).getRegistStatus()));
                ncell = nrow.createCell(6);
                ncell.setCellValue(usersInfo.get(i).getUserEmail());
                ncell = nrow.createCell(7);
                ncell.setCellValue(DateCommonUtil.getFormatDateStr(usersInfo.get(i).getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
            }
        }

        //创建excel文件
        File file = new File(resource,fileName);

        try {
            file.createNewFile();
            //将excel写入
            FileOutputStream stream= new FileOutputStream(file);
            workbook.write(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
