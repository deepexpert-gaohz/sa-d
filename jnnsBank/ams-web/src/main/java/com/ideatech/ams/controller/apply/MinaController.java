package com.ideatech.ams.controller.apply;


import com.ideatech.ams.apply.dto.ApplyOcrDto;
import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntDto;
import com.ideatech.ams.apply.entity.CompanyPreOpenAccountEnt;
import com.ideatech.ams.apply.service.ApplyOcrService;
import com.ideatech.ams.apply.service.CompanyPreOpenAccountEntService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.common.msg.ListRestResponse;
import com.ideatech.common.msg.ObjectRestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/apply/mina")
public class MinaController {

	 @Autowired
	 private CompanyPreOpenAccountEntService companyPreOpenAccountEntService;

	@Autowired
	private ApplyOcrService applyOcrService;

	@Autowired
	private ConfigService configService;

	/**
     * 保存预约信息
     * @param dto
     * @return
     */
	@RequestMapping(value = "/saveApply", method = RequestMethod.POST)
	 public ObjectRestResponse<CompanyPreOpenAccountEnt> saveApply(CompanyPreOpenAccountEntDto dto) {
	     if(StringUtils.isBlank(dto.getName())){
	        return new ObjectRestResponse().rel(false).msg("企业名称不能为空");
	     }
	     if(StringUtils.isBlank(dto.getPhone())){
	        return new ObjectRestResponse().rel(false).msg("手机号码不能为空");
	     }
	     
	     String applyId = companyPreOpenAccountEntService.saveApply(dto);
	     
	     return new ObjectRestResponse().rel(true).msg("保存成功").result(applyId);
	  }

	/**
	 * 根据预约编号获取预约信息
	 * @param applyId
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ObjectRestResponse<CompanyPreOpenAccountEntDto> list(String applyId) {
		if (StringUtils.isBlank(applyId)){
			return new ObjectRestResponse<CompanyPreOpenAccountEntDto>().rel(false);
		}

		return companyPreOpenAccountEntService.findOne(applyId);
	}

	@RequestMapping(value = "/ocr", method = RequestMethod.GET)
	@ResponseBody
	public ListRestResponse<ApplyOcrDto> getOcrList(String applyId, String docCode) {
		if (StringUtils.isBlank(applyId)){
			return new ListRestResponse<ApplyOcrDto>().rel(false);
		}

		List<ApplyOcrDto> listDto = applyOcrService.getApplyOcrList(applyId, docCode);
		return new ListRestResponse<ApplyOcrDto>().rel(true).result(listDto);
	}

	/**
	 * 获取客户工商信息区块和法人/负责人信息区块的显示配置
	 * @return
	 */
	@RequestMapping(value = "/getCustomerDivConfig", method = RequestMethod.GET)
	public ObjectRestResponse<Object> getCustomerDivConfig() {
		Map<String, Object> map = new HashMap<>();
		ConfigDto customerDivEnabled = configService.findOneByConfigKey("customerDivEnabled");
		ConfigDto legalDivEnabled = configService.findOneByConfigKey("legalDivEnabled");

		if(customerDivEnabled != null) {
			map.put("customerDivEnabled", customerDivEnabled.getConfigValue());
		}
		if(legalDivEnabled != null) {
			map.put("legalDivEnabled", legalDivEnabled.getConfigValue());
		}

		return new ObjectRestResponse<>().rel(true).result(map);
	}

}
