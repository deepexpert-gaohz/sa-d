package com.ideatech.ams.kyc.service.idcard;


import com.ideatech.ams.kyc.dao.idcard.IdCheckLogDao;
import com.ideatech.ams.kyc.dto.idcard.IdCardLocalDto;
import com.ideatech.ams.kyc.dto.idcard.IdCheckLogDto;
import com.ideatech.ams.kyc.entity.idcard.IdCheckLog;
import com.ideatech.common.util.BeanCopierUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class IdCheckLogServiceImpl implements IdCheckLogService {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private IdCardLocalService idCardLocalService;

	@Autowired
	private IdCheckLogDao idCheckLogDao;

	@Override
	public IdCheckLogDto create(IdCardLocalDto cardLocalInfo, IdCheckLogDto checkLogInfo) {
		log.info("开始记录【"+cardLocalInfo.getIdCardName()+"】核查结果！");
		IdCheckLog checkLog = new IdCheckLog();
		BeanCopierUtils.copyProperties(checkLogInfo, checkLog);
		IdCardLocalDto idCardLocalInfo =  idCardLocalService.queryByIdCardNoAndIdCardName(cardLocalInfo.getIdCardNo(), cardLocalInfo.getIdCardName());
		if(idCardLocalInfo==null){
			log.info("没有查到本地身份证信息！");
			throw new RuntimeException("没有查到本地身份证信息");
		}
		checkLog.setIdCardLocalId(idCardLocalInfo.getId());//设置本地身份信息id
		idCheckLogDao.save(checkLog);

		log.info("结束记录【"+cardLocalInfo.getIdCardName()+"】核查结果！");
		return null;
	}
	
}
