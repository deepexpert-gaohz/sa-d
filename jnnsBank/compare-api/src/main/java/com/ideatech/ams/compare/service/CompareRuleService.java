package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dto.CompareRuleDto;
import com.ideatech.ams.compare.dto.CompareRuleSearchDto;
import com.ideatech.common.service.BaseService;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author jzh
 * @date 2019/1/16.
 */

public interface CompareRuleService extends BaseService<CompareRuleDto> {

    /**
     * 查询
     * @param compareRuleSearchDto
     * @return
     */
    CompareRuleSearchDto search(CompareRuleSearchDto compareRuleSearchDto, Pageable pageable);

    /**
     * 保存
     * @param compareRuleDto
     * @param dataIds
     * @param fields
     * @param parentIds
     * @param httpServletRequest
     */
    void saveCompareRuleDetail(CompareRuleDto compareRuleDto, Long [] dataIds, Long []fields,String []parentIds, HttpServletRequest httpServletRequest);

    /**
     * 删除
     * @param id
     */
    void deleteCompareRuleDetail(Long id);

    /**
     * 查找全部
     * @return
     */
    List<CompareRuleDto> getAll();

    /**
     * 查找所有上级机构及本机构创建的规则
     */
    List<CompareRuleDto> getByOrganUpWard();
}
