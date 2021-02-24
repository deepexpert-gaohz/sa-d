package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dao.CompareFieldsDao;
import com.ideatech.ams.annual.dto.CompareFieldsDto;
import com.ideatech.ams.annual.dto.CompareRuleDto;
import com.ideatech.ams.annual.entity.CompareFields;
import com.ideatech.ams.annual.entity.CompareRule;
import com.ideatech.ams.annual.enums.CompareFieldEnum;
import com.ideatech.ams.annual.enums.DataSourceEnum;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author van
 * @date 23:31 2018/8/9
 */
@Service
public class CompareFieldsServiceImpl extends BaseServiceImpl<CompareFieldsDao, CompareFields, CompareFieldsDto> implements CompareFieldsService {
	@Value("${compare.annual.fields}")
	private String fields;

	@Autowired
	private CompareRuleService compareRuleService;

	@Override
	public List<CompareFieldsDto> listCompareRulesByTakId(Long taskId) {
		return ConverterService.convertToList(getBaseDao().findByTaskId(taskId), CompareFieldsDto.class);
	}

	@Override
	public boolean saveCompareRules(CompareFieldsDto compareFieldsDto) {
		CompareFields compareFields = ConverterService.convert(compareFieldsDto,CompareFields.class);
		if(compareFields.getId()!=null){
			compareFields.setVersionCt(getBaseDao().findOne(compareFields.getId()).getVersionCt());
		}
		if (getBaseDao().save(compareFields)!=null){
			return true;
		}
		return false;
	}

	@Override
	public void saveDefaultCompareFieldsRules(Long taskId) {
		List<CompareFieldsDto> compareFieldsDtoList = this.listCompareRulesByTakId(taskId);
		//保证数据库数据与CompareFieldEnum枚举数据一致（缺少的枚举进行补充）
		List<CompareFieldEnum> cfeList = new ArrayList<>();
		for (CompareFieldsDto cfd : compareFieldsDtoList) {
			cfeList.add(cfd.getCompareFieldEnum());
		}
		//补全年检字段
		for (CompareFieldEnum e : CompareFieldEnum.values()) {
			if (!cfeList.contains(e)) {
				CompareFieldsDto compareFieldsDto = new CompareFieldsDto();
				compareFieldsDto.setCompareFieldEnum(e);
				compareFieldsDto.setActive(isDefaultActiveField(e));
				compareFieldsDto.setTaskId(taskId);
				this.saveCompareRules(compareFieldsDto);
			}
		}
		//设置默认比对字段且是只读字段的字段  是否使用  设置成 使用，避免因前端只读，而导致不能勾选
		String lockField = this.getLockField();//获取只读年检比对字段
		for(CompareFieldsDto cfd : compareFieldsDtoList){
			if(StringUtils.isNotBlank(lockField) && lockField.contains(cfd.getCompareFieldEnum().getField())
					&& isDefaultActiveField(cfd.getCompareFieldEnum())
					&& !cfd.isActive()){
				CompareFields compareFields = getBaseDao().findOne(cfd.getId());
				compareFields.setActive(true);
				getBaseDao().save(compareFields);
			}
		}
		//创建更新默认年检规则
		compareRuleService.saveDefaultCompareRules(taskId);
	}

	/**
	 * 判断是否为默认比对字段
	 */
	private boolean isDefaultActiveField(CompareFieldEnum e){
		return e.equals(CompareFieldEnum.ACCT_NO)
				|| e.equals(CompareFieldEnum.DEPOSITOR_NAME)
				|| e.equals(CompareFieldEnum.ORGAN_CODE)
				|| e.equals(CompareFieldEnum.LEGAL_NAME)
				|| e.equals(CompareFieldEnum.REG_NO)
				|| e.equals(CompareFieldEnum.REG_ADDRESS)
				|| e.equals(CompareFieldEnum.REG_CAPITAL);
	}

	@Override
	public String getLockField() {
		return fields;
	}

	@Override
	public void setLock(CompareFieldEnum field,Long taskId) {
		CompareFields compareFields = getBaseDao().findByCompareFieldEnumAndTaskId(field,taskId);
		compareFields.setLockFiled(true);
		getBaseDao().save(compareFields);
	}

}
