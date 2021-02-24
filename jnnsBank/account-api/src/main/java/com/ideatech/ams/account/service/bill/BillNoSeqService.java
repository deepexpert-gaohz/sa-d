package com.ideatech.ams.account.service.bill;

import com.ideatech.ams.account.enums.bill.BillTypeNo;
import org.springframework.stereotype.Service;

@Service
public interface BillNoSeqService {
  
	/**
	 * @author jogyhe 
	 * 获取单据流水号
	 * 时间+机构号+业务类型编号+4位随机号
	 *  
	 * @param date
	 *            日期
	 * @param orgCode
	 *            机构代码
	 * @param type
	 * 			  单据类型代码        
	 * @throws Exception
	 */
  String getBillNo(String date, String orgCode, BillTypeNo type);
}
