package com.ideatech.ams.account.validate;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;

/**
 * 
 * @author van
 * @date 2018/5/29 11:08
 */
public interface AllPublicAccountValidate {

	void validate(AllBillsPublicDTO dto) throws Exception;
}
