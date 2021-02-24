package com.ideatech.ams.kyc.service;

import java.util.List;

import com.ideatech.ams.kyc.dto.BeneficiaryDto;
import com.ideatech.ams.kyc.dto.OutBeneficiaryDto;

public interface BeneficiaryService {
	public List<BeneficiaryDto> getBeneficiaryListBySaicInfoId(String username,Long saicInfoId, String orgfullid);

	List<BeneficiaryDto> getBeneficiaryListBySaicInfoId(String username,String orgfullid,String name);

	OutBeneficiaryDto getOutBeneficiaryDtoBySaicInfoId(String username,String orgfullid,String name);

	void insertBeneficiary(Long saicInfoId,List<BeneficiaryDto> beneficiaryList);
}
