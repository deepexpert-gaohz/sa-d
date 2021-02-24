package com.ideatech.ams.kyc.service.idcard;

import com.ideatech.ams.kyc.dto.idcard.IdCardLocalDto;
import com.ideatech.ams.kyc.dto.idcard.IdCheckLogDto;

import java.util.List;

public interface IdCheckLogService {
	/**
	 * 创建人行核查记录
	 * @param cardLocalDto
	 * @param checkLogDto
	 * @return
	 */
	IdCheckLogDto create(IdCardLocalDto cardLocalDto, IdCheckLogDto checkLogDto);

}
