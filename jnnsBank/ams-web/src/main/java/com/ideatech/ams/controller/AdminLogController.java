package com.ideatech.ams.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ideatech.ams.system.trace.aop.OperateLog;
import com.ideatech.ams.system.trace.enums.OperateModule;
import com.ideatech.ams.system.trace.enums.OperateType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ideatech.ams.system.log.service.AdminLogService;
import com.ideatech.common.dto.DhtmlxGrid;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.exception.EacException;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.FileExtraUtils;

@RestController
@RequestMapping("/adminLog")
@Slf4j
public class AdminLogController {

	@Value("${systemlog.file.location}")
	private String syslog;
	
	@Autowired
	private AdminLogService adminLogService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResultDto querySystemLog() {
		return ResultDtoFactory.toAckData(adminLogService.querySystemLog());
	}
	
	/**
	 * 下载系统日志
	 */
	@OperateLog(operateModule = OperateModule.ADMINLOG,operateType = OperateType.EXPORT)
	@RequestMapping(value = "/syslog/exportLog")
	public void exportAccountFilingExcel(HttpServletRequest request, HttpServletResponse response, String filename) {
//		filename = ReflectedXssUtils.fixAbsolute(ReflectedXssUtils.encodeHtml(filename));
		//防止文件路径被篡改
		filename = filename.replace("..", "").replace("/", "");
		log.info("下载系统日志名称：{},日志存放路径：{}" , filename,syslog);

		String outputFile = syslog + File.separator + filename;
//		String fileName = ReflectedXssUtils.fixBloack(filename);
		String fileName = filename;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(outputFile);
			outputStream = response.getOutputStream();
			response.setContentType("application/x-download");
			response.addHeader("Content-Disposition", "attachment;filename=" + FileExtraUtils.handleFileName(request, fileName));
			IOUtils.copy(inputStream, outputStream);
			outputStream.flush();
		} catch (IOException e) {
			if (e instanceof FileNotFoundException) {
				throw new EacException("没有生成文件,请联系系统管理员");
			} else {
				throw new EacException("下载异常,请联系系统管理员");
			}
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
		}
	}

	/**
	 * 批量下载
	 * 
	 * @param request
	 * @param response
	 */
	@OperateLog(operateModule = OperateModule.ADMINLOG,operateType = OperateType.EXPORT,operateContent = "打包下载")
	@RequestMapping(value = "/syslog/batchDown")
	public void batchDownLog(HttpServletRequest request, HttpServletResponse response, String beginDate, String endDate) {
//		beginDate = ReflectedXssUtils.fixBloack(ReflectedXssUtils.encodeHtml(beginDate));
//		endDate = ReflectedXssUtils.fixBloack(ReflectedXssUtils.encodeHtml(endDate));
		File[] listFiles = new File(syslog).listFiles();
		List<File> listFile = new ArrayList<File>();
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String fileName = "";
		try {
			for (File f : listFiles) {
				if (f.getName().contains("ams-web")) {
					if ("ams-web.log".equals(f.getName())) {
						continue;
					}
					fileName = f.getName().substring(8, 18);
					if (DateUtils.daysBetween(fileName, endDate) >= 0 && DateUtils.daysBetween(beginDate, fileName) >= 0) {
						listFile.add(f);
					}
				}

			}
			if (DateUtils.daysBetween(DateUtils.getNowDateShort("yyyy-MM-dd"), endDate) >= 0) {
				listFile.add(new File(syslog + File.separator + "ams-web.log"));
			}
//			String zipName = ReflectedXssUtils.fixAbsolute(beginDate + "至" + endDate + ".zip");
			String zipName = beginDate + "至" + endDate + ".zip";
			ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(new File(zipName)));
			int buffersize = 8 * 1024;
			BufferedInputStream bis = null;
			byte[] data = new byte[buffersize];
			for (File file : listFile) {
				zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
				bis = new BufferedInputStream(new FileInputStream(file));
				while ((bis.read(data)) != -1) {
					zipOutputStream.write(data, 0, data.length);
				}
				bis.close();
			}
			zipOutputStream.close();
			inputStream = new FileInputStream(new File(zipName));
			outputStream = response.getOutputStream();
			response.setContentType("application/x-download");
			response.addHeader("Content-Disposition", "attachment;filename=" + FileExtraUtils.handleFileName(request, zipName));
			IOUtils.copy(inputStream, outputStream);
			outputStream.flush();
		} catch (IOException e) {
			if (e instanceof FileNotFoundException) {
				throw new EacException("没有生成文件,请联系系统管理员");
			} else {
				throw new EacException("下载异常,请联系系统管理员");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
		}
	}

}
