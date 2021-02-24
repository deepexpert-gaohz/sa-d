/**
 * 
 */
package com.ideatech.ams.account.spi;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.system.user.dto.UserDto;

import java.util.Map;

/**
 * @author zhailiang
 *
 */
public interface AllPublicAccountFormProcessor {

	AllBillsPublicDTO process(UserDto userInfo, Map<String, String> formData);

}
