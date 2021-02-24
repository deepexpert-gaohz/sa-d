package com.ideatech.ams.kyc.service.idcard;

import com.ideatech.ams.kyc.dto.idcard.IdCheckLogDto;

import java.util.List;

/**
 * 身份信息比对接口
 * @author yang
 *
 */
public interface IdCardComperService {
	/**
	 * 核查身份证信息
	 * @param idCardNo 身份证号码
	 * @param idCardName 身份证姓名
	 */
	IdCheckLogDto comperIdCard(String idCardNo, String idCardName);

	/**
	 * 校验联网核查用户登录是否成功
	 * @param ip
	 * @param username
	 * @param password
	 * @return
	 */
	Boolean login(String ip, String username, String password);

	/**
	 * 批量上传记录
	 * @param idCardNo
	 * @param idCardName
	 */
	void save(String idCardNo, String idCardName);

	/**
	 * 开始批量联网核查
	 */
	void start();

    List<IdCheckLogDto> findAll();

    void delete(String idcardName, String idcardNo);
}
