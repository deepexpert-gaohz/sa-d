package com.ideatech.ams.account.service.core;

import org.springframework.stereotype.Service;

import com.ideatech.ams.account.dto.CorePublicAccountDto;
import com.ideatech.ams.account.dto.CorePublicAccountOuterInfo;


@Service
public interface CorePublicAccountService {

	CorePublicAccountOuterInfo save(Long userId, CorePublicAccountDto corePublicAccountDTO);

}
