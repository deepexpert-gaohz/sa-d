package com.ideatech.ams.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.kyc.dto.SaicInfoDto;
import com.ideatech.ams.kyc.dto.poi.SaicPoi;
import com.ideatech.ams.kyc.service.poi.SaicRecordExport;
import com.ideatech.common.excel.util.ExportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.*;

/**
 *
 *
 * @author van
 * @date 21:23 2018/8/29
 */
@Slf4j
public class WordTest {

	public static void word(String[] args) {
		Writer out = null;
		FileOutputStream fos = null;
		try {
			Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
			cfg.setDirectoryForTemplateLoading(
					new File("C:\\Data\\Learning\\idea\\workspace\\ams\\ams-web\\src\\main\\webapp\\static\\word"));
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			Template template = cfg.getTemplate("annual6.ftl");

			JSONObject data = JSON
					.parseObject(FileUtils.readFileToString(new File("C:\\Users\\vanto\\Desktop\\yd.json"), "UTF-8"));

			JSONArray array = new JSONArray();
			array.add(data);
			array.add(data);


			JSONObject results = new JSONObject();

			results.put("results", array);


			File outFile = new File("C:\\Users\\vanto\\Desktop\\demo.doc");
			fos = new FileOutputStream(outFile);
			// 这个地方对流的编码不可或缺，使用main（）单独调用时，应该可以，但是如果是web请求导出时导出后word文档就会打不开，并且包XML文件错误。主要是编码格式不正确，无法解析。
			out = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			template.process(results, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(fos);
		}

	}


	public static void main(String[] args) {
		OutputStream toClient = null;
		try {

			List<SaicPoi> poiList = new ArrayList<SaicPoi>();
			SaicPoi saicPoi = new SaicPoi();
			saicPoi.setAddress("aaaaaaaa");
			poiList.add(saicPoi);

			//基本信息-excel
			IExcelExport saicRecordExport = new SaicRecordExport();
			saicRecordExport.setPoiList(poiList);


			toClient = new BufferedOutputStream(new FileOutputStream(new File("C:\\Users\\vanto\\Desktop\\annual.xls")));

			ExportExcel.export(toClient, "yyyy-MM-dd", saicRecordExport);
			toClient.flush();

		} catch (Exception e) {
			log.error("", e);
		} finally {
			IOUtils.closeQuietly(toClient);
		}
	}


}
