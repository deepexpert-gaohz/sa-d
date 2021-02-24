package com.ideatech.ams.ws.api.service;

import com.ideatech.common.dto.ResultDto;

public interface SaicApiService {

	/**
	 * 检查工商返回结果
	 * @param name  操作人员用户名称
	 * @param key   企业名称/统一社会信用代码
	 * @return
	 */
	ResultDto checkSaicInfo(String name, String key);

	/**
	 * 检查工商返回结果
	 * @param name 操作人员用户名称
	 * @param key 企业名称/统一社会信用代码
	 * @param organCode 行内机构号
	 * @return
	 */
	ResultDto checkSaicInfo(String name, String key, String organCode);

	/**
	 * 查询本地工商数据
	 * @param name  名称
	 * @return
	 */
	ResultDto querySaicLocal(String name);


	/**
	 * 详细信息的工商数据查询
	 * 包含：工商信息、股权结构、受益人、基本户履历
	 *
	 *
	 * @param name
	 * @return
	 */
	ResultDto querySaicFull(String name);

	/**
	 * 查询是否进行过客户尽调
	 *
	 * @param name	企业名称(全称)
	 * @return
     */
	ResultDto queryIsKyc(String name);
}
