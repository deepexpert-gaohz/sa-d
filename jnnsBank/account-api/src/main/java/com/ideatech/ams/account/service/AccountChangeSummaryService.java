package com.ideatech.ams.account.service;

import com.alibaba.fastjson.JSONArray;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;

import java.util.List;
import java.util.Map;

public interface AccountChangeSummaryService {

    void saveAccountChangeSummary(List<String> changFieldNameList, Map<String, Object> beforeChangeFieldValueMap, Map<String, Object> afterChangeFieldValueMap, AllBillsPublicDTO account);

    Map<String, String> findByAccountChangeItems(Long billId);

    JSONArray findByAccountChangeItemsAll(Long billId);

    /**
     * 得到该笔流水变更的字段
     *
     * @param billId
     * @return
     */
    List<String> findAccountChangeFields(Long billId);

    /**
     * 保存前台提交的数据
     *
     * @param formMap
     */
    void saveAccountChangeSummary(Map<String, String> formMap);
    /**
     * 检查字段是否变更
     * @param billId
     * @param field
     * @return
     */
    String findIsChange(Long billId,String field);
}
