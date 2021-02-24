/**
 * 
 */
package com.ideatech.ams.system.log.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ideatech.ams.system.log.dto.SysLogInfo;
import com.ideatech.common.dto.DhtmlxGrid;
import com.ideatech.common.util.DateUtils;

import lombok.extern.slf4j.Slf4j;


/**
 * @author zhailiang
 *
 */
@Service
@Slf4j
public class AdminLogServiceImpl implements AdminLogService {

	private Logger log = LoggerFactory.getLogger(getClass());


	@Value("${systemlog.file.location}")
	private String syslog;


	@Override
	public List<SysLogInfo> querySystemLog() {
		List<SysLogInfo> systemlogFiles = new ArrayList<SysLogInfo>();
		File file = new File(syslog);
		if (!file.exists()) {
			file.mkdirs();
		}
		File[] files = file.listFiles();
		String fileName = "";

		for (File f : files) {

			try {
				if (f.getName().contains("ams-web")) {
					if (f.getName().equals("ams-web.log")) {
						continue;
					}
					fileName = f.getName().substring(8, 18);
					if (DateUtils.daysBetween(fileName, DateUtils.getNowDateShort("yyyy-MM-dd")) > 30) {
						f.delete();
						log.info(f.getName() + "超过一个月，日志文件已经删除。。。");
					}
				}
			} catch (Exception e) {
				log.error("删除一个月前的日志文件出错了", e);
			}
		}
		Map<Long, File> map = new HashMap<Long, File>();
		for (int j = 0; j < files.length; j++) {
			if (files[j].isFile()) {
				map.put(files[j].lastModified(), files[j]);
			}
		}
		List<Long> longTime = new ArrayList<Long>(map.keySet());
		Collections.sort(longTime);
		SysLogInfo systemLog = null;
		for (int j = 0; j < longTime.size(); j++) {
			systemLog = new SysLogInfo();
			if (j < 30) {
				String sysFileName = map.get(longTime.get(longTime.size() - 1 - j)).getName();
				// 系统日志的文件名称
				systemLog.setSyslogContent(sysFileName);
				// 系统日志的文件路径
				systemlogFiles.add(systemLog);
			}
		}
		// 调用方法生成dhtml
		return systemlogFiles;

	}

	public DhtmlxGrid getDhtmlxGrid(List<SysLogInfo> systemlogFiles) {
		DhtmlxGrid dhtmlxGrid = new DhtmlxGrid();
		// 重新设定DhtmlxGrid中的rows
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		// 列名称字符串数组
		for (int i = 0; i < systemlogFiles.size(); i++) {
			Map<String, Object> row = new HashMap<String, Object>();
			row.put("id", UUID.randomUUID());
			List<Object> data = new ArrayList<Object>();
			// data.add(systemlogFiles.get(i).getSyslogDate());
			data.add(systemlogFiles.get(i).getSyslogContent());
			data.add("下载^javascript:downloadSyslogFile(\"" + systemlogFiles.get(i).getSyslogContent() + "\");^_self");
			row.put("data", data);
			rows.add(row);
		}
		dhtmlxGrid.setRows(rows);
		return dhtmlxGrid;
	}

}
