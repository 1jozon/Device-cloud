/**
 * All rights Reserved, Designed By MiGu
 * Copyright:    Copyright(C) 2016-2020
 * Company       MiGu  Co., Ltd.
 */

package cn.rmy.common.redisUtils;


import cn.rmy.common.redisUtils.concasts.CommonConst;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目名称: common. 包: com.migu.redstone.microservice.common.cn.rmy.util 类名称:
 * CommonUtil.java 类描述: 创建人: wangqian 创建时间: 2017年7月29日 下午12:53:58
 */
public final class CommonUtil {
	/**
	 * CommonUtil 私有构造方法，解决checkstyle问题.
	 */
	private CommonUtil() {

	}

	/**
	 * .
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(cn.rmy.common.redisUtils.CommonUtil.class);


	/**
	 * 类名称: isNull. 类描述: 判断对象是否为空 创建人: shenhf 创建时间: 2017年8月28日 上午11:05:45
	 *
	 * @param obj
	 *            Object
	 * @return boolean 对象不为空，返回true.反之返回false
	 */
	public static boolean isNull(final Object obj) {
		return obj == null;
	}

	/**
	 * 类名称: isNotNull. 类描述: 判断对象是否不为空 创建人: shenhf 创建时间: 2017年8月28日 上午11:06:03
	 *
	 * @param obj
	 *            Object
	 * @return boolean
	 */
	public static boolean isNotNull(final Object obj) {
		return !isNull(obj);
	}

	/**
	 * 〈给查询加上state=1的条件〉. 〈给查询加上state=1的条件〉
	 *
	 * @param obj
	 *            [查询条件对象 ]
	 */
	public static void andState(final Object obj) {
		Class c = obj.getClass();
		try {
			Method m = c.getMethod(CommonConst.ANDSTATE.METHOD_NAME, String.class);
			m.invoke(obj, CommonConst.ANDSTATE.METHOD_VAL);
		} catch (RuntimeException e) {
			LOGGER.error("Method is null,obj" + obj.toString());
		} catch (Exception e) {
			LOGGER.error("Method is null,obj" + obj.toString());
		}
	}

	/**
	 *
	  *〈生成随机数〉
	  *〈功能详细描述〉
	  * @param
	 */
	public static String getRandomCode(int number) {
		String code = "";
		Random random = new Random();
		StringBuffer bufferStr = new StringBuffer();
		bufferStr.append(code);
		for (int i = 0; i < number; i++) {
			int r = random.nextInt(10); // 每次随机出一个数字（0-9）
			bufferStr.append(String.valueOf(r));
		}
		code = bufferStr.toString();
		return code;
	}


	/**
	 * Excel转Json
	 * @param url
	 *
	 * xsc
	 */
	public static JSONObject excelToJson(String url){
		try {
			FileInputStream inp = new FileInputStream(new File(url));
//			FileMagic fm = FileMagic.valueOf(inp);
			Workbook workbook = WorkbookFactory.create(inp);
			//获取sheet数
			int sheetNum = workbook.getNumberOfSheets();
			JSONObject jsonObject = new JSONObject();
			for (int s = 0; s < sheetNum; s++) {
				// Get the Sheet of s.
				Sheet sheet = workbook.getSheetAt(s);
				//获取最大行数
				int rownum = sheet.getPhysicalNumberOfRows();
				if (rownum <= 1) {
					continue;
				}
				//获取第一行
				Row row1 = sheet.getRow(0);
				//获取最大列数
				int colnum = row1.getPhysicalNumberOfCells();
				JSONArray jsonArray = new JSONArray();
				for (int i = 1; i < rownum; i++) {
					Row row = sheet.getRow(i);
					if (row != null) {
//                    List<Object> list = new ArrayList<>();
						JSONObject rowObj = new JSONObject();
						//循环列
						for (int j = 0; j < colnum; j++) {
							Cell cellData = row.getCell(j);
							if (cellData != null) {
								//判断cell类型
								switch (cellData.getCellType()) {
									case Cell.CELL_TYPE_NUMERIC: {
										rowObj.put(row1.getCell(j).getStringCellValue(), cellData.getNumericCellValue());
										break;
									}
									case Cell.CELL_TYPE_FORMULA: {
										//判断cell是否为日期格式
										if (DateUtil.isCellDateFormatted(cellData)) {
											//转换为日期格式YYYY-mm-dd
											rowObj.put(row1.getCell(j).getStringCellValue(), cellData.getDateCellValue());
										} else {
											//数字
											rowObj.put(row1.getCell(j).getStringCellValue(), cellData.getNumericCellValue());
										}
										break;
									}
									case Cell.CELL_TYPE_STRING: {
										rowObj.put(row1.getCell(j).getStringCellValue(), cellData.getStringCellValue());
										break;
									}
									default:
										rowObj.put(row1.getCell(j).getStringCellValue(), "");
								}
							} else {
								rowObj.put(row1.getCell(j).getStringCellValue(), "");

							}
						}
						jsonArray.add(rowObj);
					}
				}
//				.info(jsonArray.toJSONString());
				System.out.println("jsonArray----\t" + jsonArray.toJSONString());
				jsonObject.put(sheet.getSheetName(), jsonArray);
			}
			System.out.println("jsonObject----\t" + jsonObject.toJSONString());
//			logger.info(jsonObject.toJSONString());
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *	Excel转Json
	 * @param file
	 * @return
	 * xsc
	 */
	public static JSONObject excelToJson(MultipartFile file){
		try {
//			FileInputStream inp = new FileInputStream(file);
			Workbook workbook = WorkbookFactory.create(file.getInputStream());
			//获取sheet数
			int sheetNum = workbook.getNumberOfSheets();
			JSONObject jsonObject = new JSONObject();
			for (int s = 0; s < sheetNum; s++) {
				// Get the Sheet of s.
				Sheet sheet = workbook.getSheetAt(s);
				//获取最大行数
				int rownum = sheet.getPhysicalNumberOfRows();
				if (rownum <= 1) {
					continue;
				}
				//获取第一行
				Row row1 = sheet.getRow(0);
				//获取最大列数
				int colnum = row1.getPhysicalNumberOfCells();
				JSONArray jsonArray = new JSONArray();
				for (int i = 1; i < rownum; i++) {
					Row row = sheet.getRow(i);
					if (row != null) {
//                    List<Object> list = new ArrayList<>();
						JSONObject rowObj = new JSONObject();
						//循环列
						for (int j = 0; j < colnum; j++) {
							Cell cellData = row.getCell(j);
							if (cellData != null) {
								//判断cell类型
								switch (cellData.getCellType()) {
									case Cell.CELL_TYPE_NUMERIC: {
										rowObj.put(row1.getCell(j).getStringCellValue(), cellData.getNumericCellValue());
										break;
									}
									case Cell.CELL_TYPE_FORMULA: {
										//判断cell是否为日期格式
										if (DateUtil.isCellDateFormatted(cellData)) {
											//转换为日期格式YYYY-mm-dd
											rowObj.put(row1.getCell(j).getStringCellValue(), cellData.getDateCellValue());
										} else {
											//数字
											rowObj.put(row1.getCell(j).getStringCellValue(), cellData.getNumericCellValue());
										}
										break;
									}
									case Cell.CELL_TYPE_STRING: {
										rowObj.put(row1.getCell(j).getStringCellValue(), cellData.getStringCellValue());
										break;
									}
									default:
										rowObj.put(row1.getCell(j).getStringCellValue(), "");
								}
							} else {
								rowObj.put(row1.getCell(j).getStringCellValue(), "");

							}
						}
						jsonArray.add(rowObj);
					}
				}
//				.info(jsonArray.toJSONString());
				System.out.println(jsonArray.toJSONString());
				jsonObject.put(sheet.getSheetName(), jsonArray);
			}
			System.out.println(jsonObject.toJSONString());
//			logger.info(jsonObject.toJSONString());
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Json 导出为 Excel
	 * @param jsonArray
	 * @return
	 * xsc
	 */
	public static void JsonToExcel(JSONArray jsonArray,String outUrl,String fileName) throws IOException, ParseException {

		System.out.println("[JsonToExcel]  jsonArray---\t" + jsonArray.toJSONString());
		JSONObject[] jsonObjects = new JSONObject[jsonArray.size()];
//		7.JSONArray转JSONObject
		for(int i=0 ; i < jsonArray.size() ;i++)
		{
			//获取每一个JsonObject对象
			JSONObject myjObject = jsonArray.getJSONObject(i);
			jsonObjects[i] = myjObject;
			System.out.println("[JsonToExcel]  myjObject---\t" + myjObject.toJSONString());
		}
		Set<String> keys = null;
		// 创建HSSFWorkbook对象
		HSSFWorkbook wb = new HSSFWorkbook();
//		WritableWorkbook writableWorkbook = Workbook.createWorkbook(outputStream);
//		WritableSheet sheet = writableWorkbook.createSheet("sheet0");// 创建新的一页

		// 创建HSSFSheet对象
		HSSFSheet sheet = wb.createSheet("sheet0");
//
//		FileReader reader = new FileReader("c://resource.txt");
//		BufferedReader br = new BufferedReader(reader);
		String str = null;
		int roleNo = 0;
		int rowNo = 0;
		for (int k=0;k<jsonObjects.length;k++) {
//			JSONObject jsonObject = JSONObject.parseObject(str);
			// 创建HSSFRow对象
			HSSFRow row = sheet.createRow(roleNo++);
			// 创建HSSFCell对象
			if (keys == null) {
				//标题
				keys = jsonObjects[k].keySet();
				System.out.println("[JsonToExcel]  keys---\t" + keys);
				for (String s : keys) {
					HSSFCell cell = row.createCell(rowNo++);
					cell.setCellValue(s);
				}
				rowNo = 0;
				row = sheet.createRow(roleNo++);
			}

			for (String s : keys) {
				//  createTime  updateTime需要进行dataTime格式化处理然后

				HSSFCell cell = row.createCell(rowNo++);
//				String temp = jsonObjects[k].getString(s);
//				System.out.println("[JsonToExcel]  jsonObjects[k]---\t" + jsonObjects[k]);
//				if(s.equals("updateTime") || s.equals("createTime")){
//					temp = DateCommonUtil.getFormatDateStr(new Date(temp),CommonConst.DATEFORMAT.TZ_FORMAT);
//				}
//				System.out.println("[JsonToExcel]  temp---\t" + temp);
//				s = DateCommonUtil.getFormatDateStr();;
				cell.setCellValue(jsonObjects[k].getString(s));
			}
			rowNo = 0;
//			System.out.println(rowNo);

		}

		// 输出Excel文件
		FileOutputStream output = new FileOutputStream(outUrl + fileName);
		wb.write(output);
//		wb.
		output.flush();
		output.close();

	}


	/**
	 * 驼峰转下划线
	 * @param line
	 * @return
	 */
	public static String camel2Underline(String line) {
		if (line == null || "".equals(line)) {
			return "";

		}

		line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));

		StringBuffer sb = new StringBuffer();

		Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");

		Matcher matcher = pattern.matcher(line);

		while (matcher.find()) {
			String word = matcher.group();

			sb.append(word.toLowerCase());

			sb.append(matcher.end() == line.length() ? "" : "_");
		}
		return sb.toString();
	}

}
