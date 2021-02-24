package com.ideatech.common.excel.util;

import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangqingan
 * @version 23/04/2018 1:57 PM
 */
@Slf4j
public class AnnualExportExcel {

	private static Pattern NUMBER_PATTERN = Pattern.compile("^//d+(//.//d+)?$");

	/**
	 * /**
	 * 导出Excel文件
	 *
	 * @param out          输出流
	 * @param excelExports 导出的Excel相关的信息
	 * @param <T>
	 */
	public static <T> void export(OutputStream out, String pattern, Set<String> annualField, String fileName, IExcelExport<T>... excelExports)
			throws IOException {
		Workbook workbook = null;
		//根据后缀判断是03还是07版本excel
		if (FilenameUtils.getExtension(fileName).equalsIgnoreCase("xls")) {
			workbook = new HSSFWorkbook();
		} else if (FilenameUtils.getExtension(fileName).equalsIgnoreCase("xlsx")) {
			workbook = new SXSSFWorkbook();
		} else {
			log.error("导出年检excel文件名不正确");
			return;
		}

		for (IExcelExport<T> excelExport : excelExports) {
			export(annualField,workbook, excelExport, pattern);
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			log.error("生成Excel出错", e);
		} finally {
			workbook.close();
		}
	}

	/**
	 * 导出Excel文件
	 *
	 * @param workbook excel 工作簿
	 * @param excelExport 导出的Excel相关的信息
	 * @param pattern     @eg. yyyy-MM-dd
	 * @param <T>
	 */
	public static <T> void export(Set<String> annualField, Workbook workbook, IExcelExport<T> excelExport, String pattern) {
		//读取配置
		List<List<ExcelHeadDto>> headList = excelExport.getHeader();
		//        String[] headers = excelExport.getHeader();

		Collection<T> dataset = excelExport.getPoiList();


		// 生成一个表格
		Sheet sheet = workbook.createSheet(excelExport.getTitle());
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);

		// 生成一个样式
		CellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.WHITE.index);
		// style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		// 生成一个字体
		Font font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		CellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		// style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(CellStyle.BORDER_THIN);
		style2.setBorderLeft(CellStyle.BORDER_THIN);
		style2.setBorderRight(CellStyle.BORDER_THIN);
		style2.setBorderTop(CellStyle.BORDER_THIN);
		style2.setAlignment(CellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// style2.setWrapText(true);
		// 生成另一个字体
		Font font2 = workbook.createFont();
		font2.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		Font font3 = workbook.createFont();
		font3.setColor(HSSFColor.BLACK.index);
		// 声明一个画图的顶级管理器
		Drawing patriarch = sheet.createDrawingPatriarch();

		// 产生表格标题行

		Row row = null;

		//先设置样式才能创建row，需要遍历2次
		int index = 0;
		for (List<ExcelHeadDto> excelHeadDtos : headList) {
			int colIndex = 0;
			for (ExcelHeadDto excelHeadDto : excelHeadDtos) {
				if (excelHeadDto.getCellRange()) {
					sheet.addMergedRegion(new CellRangeAddress(index, index + excelHeadDto.getRow() - 1, colIndex,
							colIndex + excelHeadDto.getCol() - 1));
					colIndex += excelHeadDto.getCol();
				} else {
					colIndex++;
				}
			}
			index++;
		}

		index = 0;
		//设置表头
		for (List<ExcelHeadDto> excelHeadDtos : headList) {
			int colIndex = 0;
			//先创建行
			row = sheet.createRow(index);
			//设置所有col的外边框
			for (int i = 0; i < excelExport.getColSize(); i++) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(style);
			}

			for (ExcelHeadDto excelHeadDto : excelHeadDtos) {
				Cell cell = row.createCell(colIndex);
				cell.setCellStyle(style);
//				HSSFRichTextString text = new HSSFRichTextString(excelHeadDto.getContent());
				cell.setCellValue(excelHeadDto.getContent());
				if (excelHeadDto.getCellRange()) {
					//设置下一个列编号
					colIndex += excelHeadDto.getCol();
				} else {
					colIndex++;
				}
			}
			//设置下一行的序号
			index++;
		}


		if (dataset == null || dataset.isEmpty()) {
			//空数据 直接退出
		}else{
			// 遍历集合数据，产生数据行
			Iterator<T> it = dataset.iterator();

			//设置数据从第几行开始
			if (excelExport.getDataBeginRow() != 0) {
				index = excelExport.getDataBeginRow();
			}

			while (it.hasNext()) {
				row = sheet.createRow(index);
				T t = it.next();
				// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
				Field[] fields = t.getClass().getDeclaredFields();

				String[] poiFields = getPoiFields(fields,annualField);
				for (int i = 0; i < poiFields.length; i++) {
//					Field field = fields[i];
					String fieldName = poiFields[i];
					Cell cell = row.createCell(i);
					cell.setCellStyle(style2);
					String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					try {
						Class tCls = t.getClass();
						Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
						Object value = getMethod.invoke(t, new Object[]{});
						// 判断值的类型后进行强制类型转换
						String textValue = null;
						if (excelExport.containSpecialField(fieldName)) {
							textValue = excelExport.getSpecialFieldValue(fieldName);
						} else if (value instanceof Date) {
							Date date = (Date) value;
							SimpleDateFormat sdf = new SimpleDateFormat(pattern);
							textValue = sdf.format(date);
						} else if (value instanceof Long) {
							Date date = new Date((Long) value);
							SimpleDateFormat sdf = new SimpleDateFormat(pattern);
							textValue = sdf.format(date);
						} else if (value instanceof byte[]) {
							// 有图片时，设置行高为60px;
							row.setHeightInPoints(60);
							// 设置图片所在列宽度为80px,注意这里单位的一个换算
							sheet.setColumnWidth(i, (short) (35.7 * 80));
							// sheet.autoSizeColumn(i);
							byte[] bsValue = (byte[]) value;
//							ClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6,
//									index);
//							anchor.setAnchorType(2);
//							patriarch.createPicture(anchor, workbook.addPicture(bsValue, Workbook.PICTURE_TYPE_JPEG));
						} else {
							// 其它数据类型都当作字符串简单处理
							if (value == null) {
								textValue = "";
							} else {
								textValue = value.toString();
							}
						}
						// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
						if (textValue != null) {
							Matcher matcher = NUMBER_PATTERN.matcher(textValue);
							if (matcher.matches()) {
								// 是数字当作double处理
								cell.setCellValue(Double.parseDouble(textValue));
							} else {
//								HSSFRichTextString richString = new HSSFRichTextString(textValue);
//								richString.applyFont(font3);
								cell.setCellValue(textValue);
							}
						}
					} catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						log.error("生成Excel出错", e);
					} finally {
						// 清理资源
					}
				}
			for (int i = 0; i < poiFields.length; i++) {
//				Field field = fields[i];
				String fieldName = poiFields[i];
				Cell cell = row.createCell(i);
				cell.setCellStyle(style2);
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				try {
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
					Object value = getMethod.invoke(t, new Object[]{});
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if (excelExport.containSpecialField(fieldName)) {
						textValue = excelExport.getSpecialFieldValue(fieldName);
					} else if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					} else if (value instanceof Long) {
						Date date = new Date((Long) value);
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					} else if (value instanceof byte[]) {
						// 有图片时，设置行高为60px;
						row.setHeightInPoints(60);
						// 设置图片所在列宽度为80px,注意这里单位的一个换算
						sheet.setColumnWidth(i, (short) (35.7 * 80));
						// sheet.autoSizeColumn(i);
						byte[] bsValue = (byte[]) value;
//						HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6, index);
//						anchor.setAnchorType(2);
//						patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
					} else {
						// 其它数据类型都当作字符串简单处理
						if (value == null) {
							textValue = "";
						} else {
							textValue = value.toString();
						}
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Matcher matcher = NUMBER_PATTERN.matcher(textValue);
						if (matcher.matches()) {
							// 是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
//							HSSFRichTextString richString = new HSSFRichTextString(textValue);
//							richString.applyFont(font3);
							cell.setCellValue(textValue);
						}
					}
				} catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					log.error("生成Excel出错", e);
				} finally {
					// 清理资源
				}
			}

				index++;
			}
		}

		// 内容自适应
		for (int i : excelExport.getAutoSizeColumns()) {
			sheet.autoSizeColumn(i);
		}
	}

	public static String[] getPoiFields(Field[] fields,Set<String> annualFields){
		//获取人行年检字段
		List<String> fieldsList = new ArrayList<>();
		List<String> notContains = new ArrayList<>();
		for(Field field : fields){
			fieldsList.add(field.getName());
		}
		//找出不一致的字段
		for(String a : fieldsList){
			if(StringUtils.equals("coreOrganCode",a) || StringUtils.equals("saicOrganCode",a) || StringUtils.equals("pbcOrganCode",a)){
				continue;
			}
			if(!annualFields.contains(a)){
				notContains.add(a);
			}
		}
		fieldsList.removeAll(notContains);
		//增加公共部分字段
		fieldsList.add("acctType");
		fieldsList.add("organCode");
		fieldsList.add("unilateral");
		fieldsList.add("abnormal");
		fieldsList.add("black");
		fieldsList.add("saicStatus");
		fieldsList.add("match");
		fieldsList.add("result");
		fieldsList.add("pbcSubmitter");

		String[] poiFields = new String[fieldsList.size()];
		fieldsList.toArray(poiFields);
		return poiFields;
	}
}
