package com.ideatech.ams.kyc.service.idcard;

import com.ideatech.ams.kyc.dto.idcard.IdCardLocalDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IdCardLocalService {
	/**
	 * 保存更新本人身份证信息
	 * @param Dto
	 * @return
	 */
	void save(IdCardLocalDto Dto);
	
	/**
	 * 查询本地省份证信息
	 */
	List<IdCardLocalDto> query(IdCardLocalDto Dto);
	/**
	 * 删除本地身份信息
	 */
	void delete(long id);
	/**
	 * 
	 * @param idCardNo
	 * @param idCardName
	 * @return
	 */
	IdCardLocalDto queryByIdCardNoAndIdCardName(String idCardNo, String idCardName);
	
	IdCardLocalDto getIdCardById(Long id);
	
	IdCardLocalDto findOne(Long id);

	List<IdCardLocalDto> findByOrganFullIdLike(String organFullId);
}
