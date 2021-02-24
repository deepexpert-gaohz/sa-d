package com.ideatech.ams.account.service.core;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.system.user.dto.UserDto;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

/**
 * 核心数据初始化
 * @author yang
 *
 */
public interface AmsCoreAccountService {
	/**
	 * 确定核心全量数据文件是否存在
	 *
	 * @return
	 */
	boolean isHaveFile();

	/**
	 * 初始化文件
	 */
	void initData();

	void initTableData();

	HashMap<String, Integer> arrayToCorePublicAccount(String[] methodNames, String[] values, Object obj, Map<String, Integer> fieldLengthForStringMap);

	void saveAllBillsPublic(Page list, UserDto userInfo) throws Exception;

	AllBillsPublicDTO convertDictionary(Map<String, String> hashMap);

	void initAllBillsPublicData(AllBillsPublicDTO allBillsPublicDTO);

	void checkIsNull(AllBillsPublicDTO allBillsPublicDTO, UserDto userInfo);
}
