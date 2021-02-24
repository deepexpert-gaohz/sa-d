package com.ideatech.ams.kyc.service;

import java.util.List;

import com.ideatech.ams.kyc.dto.BaseAccountDto;

public interface BaseAccountService {
	List<BaseAccountDto> getBaseAccountListBySaicInfoId(String username,Long saicInfoId, String orgfullid);
}

