package com.ideatech.ams.account.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dao.AccountChangeItemDao;
import com.ideatech.ams.account.dao.AccountChangeSummaryDao;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.entity.AccountChangeItem;
import com.ideatech.ams.account.entity.AccountChangeSummary;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.EnameToCnameEnum;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.*;

@Service
@Transactional
@Slf4j
public class AccountChangeSummaryServiceImpl implements AccountChangeSummaryService {

    @Autowired
    private AccountChangeSummaryDao accountChangeSummaryDao;

    @Autowired
    private AccountChangeItemDao accountChangeItemDao;

    @Override
    public void saveAccountChangeSummary(List<String> changFieldNameList, Map<String, Object> beforeChangeFieldValueMap, Map<String, Object> afterChangeFieldValueMap, AllBillsPublicDTO account) {
        // 变更概括表
        AccountChangeSummary changeSummary = accountChangeSummaryDao.findByRefBillId(account.getRefBillId());
        if (changeSummary == null) {
            changeSummary = new AccountChangeSummary();
            changeSummary.setRefBillId(account.getRefBillId());
            changeSummary.setAcctNo(account.getAcctNo());
            changeSummary.setAcctType(account.getAcctType());
            changeSummary.setOperateName(SecurityUtils.getCurrentUsername());
            changeSummary.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
            accountChangeSummaryDao.save(changeSummary);
        }


        for(String key : changFieldNameList) {
            AccountChangeItem accountChangeItem = new AccountChangeItem();
            accountChangeItem.setChangeSummaryId(changeSummary.getId());
            accountChangeItem.setColumnName(key);
            accountChangeItem.setOldValue(beforeChangeFieldValueMap.get(key) == null ? "" : beforeChangeFieldValueMap.get(key).toString());
            accountChangeItem.setNewValue(afterChangeFieldValueMap.get(key) == null ? "" : afterChangeFieldValueMap.get(key).toString());
            log.info("账号{}变更字段保存：{};oldValue：{};newValue：{}",account.getAcctNo(),key,accountChangeItem.getOldValue(),accountChangeItem.getNewValue());

            // 判断变更项是否已经存在
            AccountChangeItem changeItemData = accountChangeItemDao.findByChangeSummaryIdAndColumnName(changeSummary.getId(), key);
            if (changeItemData == null) {
                accountChangeItemDao.save(accountChangeItem);
            } else {
                accountChangeItem.setId(changeItemData.getId());
                ConverterService.convert(accountChangeItem, changeItemData);
                accountChangeItemDao.save(changeItemData);
            }
        }

    }

    @Override
    public Map<String, String> findByAccountChangeItems(Long billId){
    	Map<String, String> map = new HashMap<String,String>();
    	AccountChangeSummary changeSummary = accountChangeSummaryDao.findByRefBillId(billId);
    	if(changeSummary != null) {
    		List<AccountChangeItem> list = accountChangeItemDao.findByChangeSummaryId(changeSummary.getId());
    		for (AccountChangeItem accountChangeItem : list) {
    			map.put(accountChangeItem.getColumnName(), accountChangeItem.getOldValue());
			}
    	}
    	return map;
    }

    @Override
    public JSONArray findByAccountChangeItemsAll(Long billId){
        JSONArray jsonArray = new JSONArray();
    	AccountChangeSummary changeSummary = accountChangeSummaryDao.findByRefBillId(billId);
    	if(changeSummary != null) {
    		List<AccountChangeItem> list = accountChangeItemDao.findByChangeSummaryId(changeSummary.getId());
    		for (AccountChangeItem accountChangeItem : list) {
    		    if(accountChangeItem.getColumnName().equals("organFullId")){
                    continue;
                }
    		    JSONObject jsonObject = new JSONObject();
    		    if(accountChangeItem.getColumnName().contains("companyPartnerInfoSet")){
    		        String name = accountChangeItem.getColumnName().replace("[","").replace("]","").replace(".","");
                    jsonObject.put("cname", Objects.requireNonNull(EnameToCnameEnum.str2enum(name)).getValue());//中文名
                }else{
                    jsonObject.put("cname", Objects.requireNonNull(EnameToCnameEnum.str2enum(accountChangeItem.getColumnName())).getValue());//中文名
                }
                jsonObject.put("name", accountChangeItem.getColumnName());//英文名
                if(StringUtils.isNotBlank(accountChangeItem.getOldValue())){
                    jsonObject.put("oldValue", accountChangeItem.getOldValue());//变更前数据
                }else{
                    jsonObject.put("oldValue", "");//变更前数据
                }
                if(StringUtils.isNotBlank(accountChangeItem.getNewValue())){
                    jsonObject.put("newValue", accountChangeItem.getNewValue());//变更后数据
                }else{
                    jsonObject.put("newValue", "");//变更后数据
                }
                jsonArray.add(jsonObject);
			}
			return jsonArray;
    	}
    	return jsonArray;
    }

    @Override
    public List<String> findAccountChangeFields(Long billId) {
        List<String> fields = new ArrayList<>(16);
        AccountChangeSummary changeSummary = accountChangeSummaryDao.findByRefBillId(billId);
        if (changeSummary != null) {
            List<AccountChangeItem> list = accountChangeItemDao.findByChangeSummaryId(changeSummary.getId());
            for (AccountChangeItem changeItem : list) {
                fields.add(changeItem.getColumnName());
            }
        }
        return fields;
    }

    @Override
    public void saveAccountChangeSummary(Map<String, String> formMap) {
        String changeFields = formMap.get("changeFields");

        if (StringUtils.isNotBlank(changeFields)) {
            JSONObject jsonObject = JSON.parseObject(changeFields);
            String billId = formMap.get("refBillId");
            if (StringUtils.isNotBlank(billId)) {
                // 变更概括表
                AccountChangeSummary changeSummary = accountChangeSummaryDao.findByRefBillId(Long.parseLong(billId));
                if (changeSummary == null) {
                    changeSummary = new AccountChangeSummary();
                    changeSummary.setRefBillId(Long.parseLong(billId));
                    changeSummary.setAcctNo(formMap.get("acctNo"));
                    changeSummary.setAcctType(CompanyAcctType.str2enum(formMap.get("acctType")));
                    changeSummary.setOperateName(SecurityUtils.getCurrentUsername());
                    changeSummary.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
                    accountChangeSummaryDao.save(changeSummary);
                }
                //审核json为0  获取原始值
                if(changeSummary != null && jsonObject.size() == 0){
                    List<AccountChangeItem> changeItems = accountChangeItemDao.findByChangeSummaryId(changeSummary.getId());
                    if(CollectionUtils.isNotEmpty(changeItems)){
                        for(AccountChangeItem accountChangeItem : changeItems){
                            jsonObject.put(accountChangeItem.getColumnName(),accountChangeItem.getOldValue());
                        }
                    }
                }
                for (String key : jsonObject.keySet()) {
                    AccountChangeItem accountChangeItem = new AccountChangeItem();
                    accountChangeItem.setChangeSummaryId(changeSummary.getId());
                    accountChangeItem.setColumnName(key);
                    accountChangeItem.setOldValue(jsonObject.get(key) == null ? "" : jsonObject.get(key).toString());
                    accountChangeItem.setNewValue(formMap.get(key) == null ? "" : formMap.get(key).toString());
                    // 判断变更项是否已经存在
                    AccountChangeItem changeItemData = accountChangeItemDao.findByChangeSummaryIdAndColumnName(changeSummary.getId(), key);
                    if (changeItemData == null) {
                        accountChangeItemDao.save(accountChangeItem);
                    } else {
                        //增加判断  如果最新修改值跟原始值是一致的 就删除该变更字段
                        if(StringUtils.equals(accountChangeItem.getOldValue(),formMap.get(key) == null ? "" : formMap.get(key).toString())){
                            accountChangeItemDao.delete(changeItemData);
                        }else{
                            accountChangeItem.setId(changeItemData.getId());
                            ConverterService.convert(accountChangeItem, changeItemData);
                            accountChangeItemDao.save(changeItemData);
                        }
                    }
                }
            }

        }
    }
    @Override
    public String findIsChange(Long billId, String field) {
        AccountChangeSummary changeSummary = accountChangeSummaryDao.findByRefBillId(billId);
        if(changeSummary!=null){
            AccountChangeItem item = accountChangeItemDao.findByChangeSummaryIdAndColumnName(changeSummary.getId(),field);
            if(item!=null){
                return item.getNewValue();
            }
        }
        return null;
    }
}
