/**
 * 
 */
package com.ideatech.ams.account.spi.processor;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.spi.AllPublicAccountFormProcessor;
import com.ideatech.ams.system.user.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author zhailiang
 *
 */
public abstract class AbstractAllPublicAccountFormProcessor implements AllPublicAccountFormProcessor {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public AllBillsPublicDTO process(UserDto userInfo, Map<String, String> formData) {
		logger.info("开始处理表单:" + formData.get("acctType") + "-" + formData.get("billType") + " 处理器:" + getClass().getSimpleName());
		AllBillsPublicDTO result = doProcess(userInfo, formData);
		logger.info("处理表单:" + formData.get("acctType") + "-" + formData.get("billType") + "完毕");
		return result;
	}

	/**
	 * @param userInfo
	 * @param formData
	 * @return
	 */
	protected abstract AllBillsPublicDTO doProcess(UserDto userInfo, Map<String, String> formData);

}
