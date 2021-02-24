package com.ideatech.ams.compare.spi.comparator;

import com.ideatech.ams.compare.dto.*;
import com.ideatech.ams.compare.dto.compareresultdetail.CompareResultDetailsField;
import com.ideatech.ams.compare.dto.data.CompareDataDto;

import java.util.List;
import java.util.Map;

/**
 * 比对器
 *
 * @author fantao
 */
public interface Comparator {

    /**
     * 比对
     *
     * @param define          所有字段详细规则
     * @param compareRuleFieldsDto 字段
     * @param compareDatas    比对数据
     * @return
     * @throws Exception
     */
    CompareResultDetailsField compare(List<CompareDefineDto> define, CompareRuleFieldsDto compareRuleFieldsDto, Map<Long, CompareDataDto> compareDatas) throws Exception;

}
