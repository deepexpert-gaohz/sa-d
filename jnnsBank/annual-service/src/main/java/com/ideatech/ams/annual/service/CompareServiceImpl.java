package com.ideatech.ams.annual.service;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.annual.dto.*;
import com.ideatech.ams.annual.enums.DataSourceEnum;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.pbc.utils.NumberUtils;
import com.ideatech.common.util.BeanValueUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *
 *
 * @author van
 * @date 19:53 2018/8/9
 */
@Service
@Slf4j
public class CompareServiceImpl implements CompareService {


	@Override
	public Boolean compare(AnnualResultDto annualResultDto, List<CompareFieldsDto> compareFieldsDtoList, Map<String, Map<String, CompareRuleDto>> compareRuleDtoMap) {
		Map<String,Object> resultMap = new HashMap<>();
		List<CompareDataDto> compareDataDtos = new ArrayList<>(3);
		compareDataDtos.add(transformCoreData(annualResultDto.getCoreData()));
		compareDataDtos.add(transformPbcData(annualResultDto.getPbcData()));
		compareDataDtos.add(transformSaicData(annualResultDto.getSaicData()));
		boolean resultFlag = true;
		//set集合  保存比对过的数据源
		Set<String> dataSourceList = new HashSet<>();
		for (CompareDataDto compareDataDto1 : compareDataDtos) {
			String dataSource = compareDataDto1.getDataSource().getValue();
			for (CompareDataDto compareDataDto2 : compareDataDtos) {
				if (compareDataDto1.getDataSource() == compareDataDto2.getDataSource()) {
					continue;
				}
				//比对过的数据源不进行重复的比对
				if(dataSourceList.contains(compareDataDto2.getDataSource().getValue())){
					continue;
				}
				//比对
				if (!compare(compareDataDto1, compareDataDto2, compareFieldsDtoList, compareRuleDtoMap, resultMap)) {
					annualResultDto.setCompareResult(JSON.toJSONString(resultMap));
					resultFlag = false ;
				}
			}
			//比对过的数据源加入set集合中
			dataSourceList.add(dataSource);
			//只循环一次
//			break;
		}
		if(!resultFlag){
			return resultFlag;
		}
		annualResultDto.setCompareResult(JSON.toJSONString(resultMap));
		return true;
	}

	@Override
	public Map<String, CompareDataDto> getCompareDatas(AnnualResultDto annualResultDto) {
		Map<String, CompareDataDto> compareDataDtoMap = new HashMap<>(3);
		compareDataDtoMap.put(DataSourceEnum.CORE.name(), transformCoreData(annualResultDto.getCoreData()));
		compareDataDtoMap.put(DataSourceEnum.PBC.name(), transformPbcData(annualResultDto.getPbcData()));
		compareDataDtoMap.put(DataSourceEnum.SAIC.name(), transformSaicData(annualResultDto.getSaicData()));
		return compareDataDtoMap;
	}

	private Boolean compare(CompareDataDto data1, CompareDataDto data2, List<CompareFieldsDto> compareFieldsDtoList, Map<String, Map<String, CompareRuleDto>> compareRuleDtoMap, Map<String,Object> resultMap) {
		//最终结果
		boolean result = true;
		//循环所有要比的字段
		for (CompareFieldsDto compareFieldsDto : compareFieldsDtoList) {
			String name = compareFieldsDto.getCompareFieldEnum().name();
			String fieldName = compareFieldsDto.getCompareFieldEnum().getField();
			//如果没存放改字段，默认成功
			if (!resultMap.containsKey(name)) {
				resultMap.put(compareFieldsDto.getCompareFieldEnum().name(), true);
			}
			if (compareFieldsDto.isActive()) {
				//该字段的比对规则
				CompareRuleDto fieldRule1 = compareRuleDtoMap.get(data1.getDataSource().name()).get(fieldName);
				CompareRuleDto fieldRule2 = compareRuleDtoMap.get(data2.getDataSource().name()).get(fieldName);

				//无此规则也跳过
				if (fieldRule1 == null || fieldRule2 == null) {
					continue;
				}

				//如果该字段不需要比对则直接跳过
				if (!fieldRule1.isActive() || !fieldRule2.isActive()) {
					continue;
				}

				String fieldValue1 = BeanValueUtils.getValueString(data1, fieldName);
				String fieldValue2 = BeanValueUtils.getValueString(data2, fieldName);

				if ("registeredCapital".equals(fieldName)) {
					fieldValue1 = StringUtils.isBlank(fieldValue1) ? "0" : fieldValue1;
					fieldValue2 = StringUtils.isBlank(fieldValue2) ? "0" : fieldValue2;

					//若注册资金为0，同时空为过，则跳过。因为当注册资金为空时被上面一步覆盖成"0"，导致下一步空为过无法跳过，所以增加此步。
					boolean nullPass = ("0".equals(fieldValue1) && fieldRule1.isNullpass()) || ("0".equals(fieldValue2) && fieldRule2.isNullpass());
					if(nullPass){
						continue;
					}
				}

				//如果空算对且是空的，则直接跳过
				boolean nullPass = (fieldRule1.isNullpass() && StringUtils.isEmpty(fieldValue1)) || (fieldRule2.isNullpass() && StringUtils.isEmpty(fieldValue2));
				if (nullPass) {
					continue;
				}

				/*//不是空算过为空直接比对不一致
				if (StringUtils.isEmpty(fieldValue1) || StringUtils.isEmpty(fieldValue2)) {
					resultMap.put(compareFieldsDto.getCompareFieldEnum().name(), false);
					result = false;
				}*/

				//比较
				if (!ObjectUtils.equals(fieldValue1, fieldValue2)) {
					resultMap.put(compareFieldsDto.getCompareFieldEnum().name(), false);
					result = false;
				}
			}
		}
		return result;
	}

	private CompareDataDto transformCoreData(String data) {
		CompareDataDto compareDataDto = new CompareDataDto();
		compareDataDto.setDataSource(DataSourceEnum.CORE);
		if (StringUtils.isNotBlank(data)) {
			CoreCollectionDto coreData = JSON.parseObject(data, CoreCollectionDto.class);
			compareDataDto.setAcctNo(coreData.getAcctNo());
			compareDataDto.setDepositorName(coreData.getDepositorName());
			compareDataDto.setLegalName(coreData.getLegalName());
			compareDataDto.setRegNo(coreData.getRegNo());
			compareDataDto.setAccountStatus(coreData.getAccountStatus());
			compareDataDto.setBusinessScope(coreData.getBusinessScope());
			compareDataDto.setRegAddress(coreData.getRegFullAddress());
			compareDataDto.setRegisteredCapital(coreData.getRegisteredCapital() == null ? null : NumberUtils.formatCapital(coreData.getRegisteredCapital()).toString());
			compareDataDto.setLegalIdcardType(coreData.getLegalIdcardType());
			compareDataDto.setLegalIdcardNo(coreData.getLegalIdcardNo());
			compareDataDto.setRegCurrencyType(coreData.getRegCurrencyType());
			compareDataDto.setStateTaxRegNo(coreData.getStateTaxRegNo());
			compareDataDto.setOrgCode(coreData.getOrgCode());
			compareDataDto.setTaxRegNo(coreData.getTaxRegNo());
			compareDataDto.setOrganCode(coreData.getOrganCode());
		}
		return compareDataDto;
	}

	private CompareDataDto transformPbcData(String data) {
		CompareDataDto compareDataDto = new CompareDataDto();
		compareDataDto.setDataSource(DataSourceEnum.PBC);
		if (StringUtils.isNotBlank(data)) {
			FetchPbcInfoDto pbcData = JSON.parseObject(data, FetchPbcInfoDto.class);
			compareDataDto.setAcctNo(pbcData.getAcctNo());
			compareDataDto.setDepositorName(pbcData.getDepositorName());
			compareDataDto.setLegalName(pbcData.getLegalName());
			compareDataDto.setRegNo(pbcData.getFileNo());
			compareDataDto.setAccountStatus(pbcData.getAccountStatus().name());
			compareDataDto.setBusinessScope(pbcData.getBusinessScope());
			compareDataDto.setRegAddress(pbcData.getRegAddress());
			compareDataDto.setRegisteredCapital(pbcData.getRegisteredCapital());
			compareDataDto.setLegalIdcardType(pbcData.getLegalIdcardType());
			compareDataDto.setLegalIdcardNo(pbcData.getLegalIdcardNo());
			compareDataDto.setRegCurrencyType(pbcData.getRegCurrencyType());
			compareDataDto.setStateTaxRegNo(pbcData.getStateTaxRegNo());
			compareDataDto.setOrgCode(pbcData.getOrgCode());
			compareDataDto.setTaxRegNo(pbcData.getTaxRegNo());
			compareDataDto.setOrganCode("");
		}
		return compareDataDto;
	}

	private CompareDataDto transformSaicData(String data) {
		CompareDataDto compareDataDto = new CompareDataDto();
		compareDataDto.setDataSource(DataSourceEnum.SAIC);
		if (StringUtils.isNotBlank(data)) {
			SaicIdpInfo saicData = JSON.parseObject(data, SaicIdpInfo.class);
			compareDataDto.setAcctNo("");
			compareDataDto.setDepositorName(saicData.getName());
			compareDataDto.setLegalName(saicData.getLegalperson());
			compareDataDto.setRegNo(StringUtils.isBlank(saicData.getUnitycreditcode()) ?
					saicData.getRegistno() :
					saicData.getUnitycreditcode());
			compareDataDto.setAccountStatus(saicData.getState());
			compareDataDto.setBusinessScope(saicData.getScope());
			compareDataDto.setRegAddress(saicData.getAddress());
			compareDataDto.setRegisteredCapital(saicData.getRegistfund());
			compareDataDto.setLegalIdcardType("");
			compareDataDto.setLegalIdcardNo("");
			compareDataDto.setRegCurrencyType("");
			compareDataDto.setStateTaxRegNo("");
			compareDataDto.setTaxRegNo("");
			if(StringUtils.isNotBlank(saicData.getUnitycreditcode())){
				compareDataDto.setOrgCode(saicData.getUnitycreditcode().length() == 18 ? StringUtils.substring(saicData.getUnitycreditcode(), 8, 17) : "");
			}else{
				compareDataDto.setOrgCode("");
			}
			compareDataDto.setOrganCode("");
		}
		return compareDataDto;
	}
}
