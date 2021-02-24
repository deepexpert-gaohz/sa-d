package com.ideatech.ams.annual.service.export;

/**
 *
 *
 * @author van
 * @date 16:05 2018/8/31
 */
public interface AnnualResultExportService {


	/**
	 * 导出年检结果数据
	 * 包含excel与word
	 * @param taskId
	 */
	void createAnnualResultExport(Long taskId);

}
