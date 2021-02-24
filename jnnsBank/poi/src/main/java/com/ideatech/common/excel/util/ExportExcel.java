package com.ideatech.common.excel.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ideatech.common.excel.dto.ExcelHeadDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.ideatech.common.excel.util.service.IExcelExport;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

/**
 * @author wangqingan
 * @version 23/04/2018 1:57 PM
 */
@Slf4j
public class ExportExcel {

	private static Pattern NUMBER_PATTERN = Pattern.compile("^//d+(//.//d+)?$");

	/**
	 * /**
	 * 导出Excel文件
	 *
	 * @param out          输出流
	 * @param excelExports 导出的Excel相关的信息
	 * @param <T>
	 */
	public static <T> void export(OutputStream out, String pattern, IExcelExport<T>... excelExports)
			throws IOException {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		for (IExcelExport<T> excelExport : excelExports) {
			export(workbook, excelExport, pattern);
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
	 *
	 * 导出Excel文件
	 *
	 * @param out          输出流
	 * @param excelExportList 导出的Excel相关的信息
	 * @param <T>
	 */
	public static <T> void export(OutputStream out, String pattern, List<IExcelExport> excelExportList)
			throws IOException {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		for (IExcelExport<T> excelExport : excelExportList) {
			export(workbook, excelExport, pattern);
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
	public static <T> void export(HSSFWorkbook workbook, IExcelExport<T> excelExport, String pattern) {
		//读取配置

		List<List<ExcelHeadDto>> headList = excelExport.getHeader();
		//        String[] headers = excelExport.getHeader();

		Collection<T> dataset = excelExport.getPoiList();


		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(excelExport.getTitle());
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);

		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.WHITE.index);
		// style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		// style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// style2.setWrapText(true);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		HSSFFont font3 = workbook.createFont();
		font3.setColor(HSSFColor.BLACK.index);
		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 产生表格标题行

		HSSFRow row = null;

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
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style);
			}

			for (ExcelHeadDto excelHeadDto : excelHeadDtos) {
				HSSFCell cell = row.createCell(colIndex);
				cell.setCellStyle(style);
				HSSFRichTextString text = new HSSFRichTextString(excelHeadDto.getContent());
				cell.setCellValue(text);
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

				for (int i = 0; i < fields.length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(style2);
					Field field = fields[i];
					String fieldName = field.getName();
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
							HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6,
									index);
							anchor.setAnchorType(2);
							patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
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
								HSSFRichTextString richString = new HSSFRichTextString(textValue);
								richString.applyFont(font3);
								cell.setCellValue(richString);
							}
						}
					} catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						log.error("生成Excel出错", e);
					} finally {
						// 清理资源
					}
				}
			for (int i = 0; i < fields.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style2);
				Field field = fields[i];
				String fieldName = field.getName();
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
						HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6,
								index);
						anchor.setAnchorType(2);
						patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
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
							HSSFRichTextString richString = new HSSFRichTextString(textValue);
							richString.applyFont(font3);
							cell.setCellValue(richString);
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
}
