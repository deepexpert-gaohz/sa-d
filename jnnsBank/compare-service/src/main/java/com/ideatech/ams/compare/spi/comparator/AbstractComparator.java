/**
 *
 */
package com.ideatech.ams.compare.spi.comparator;

import com.ideatech.ams.compare.dto.CompareDefineDto;
import com.ideatech.ams.compare.dto.CompareRuleFieldsDto;
import com.ideatech.ams.compare.dto.compareresultdetail.CompareResultDetailsField;
import com.ideatech.ams.compare.dto.data.CompareDataDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 比对器抽象类
 * @author fantao
 */
@Slf4j
public abstract class AbstractComparator implements Comparator {

    @Override
    public CompareResultDetailsField compare(List<CompareDefineDto> define, CompareRuleFieldsDto compareRuleFieldsDto, Map<Long, CompareDataDto> compareDatas) throws Exception {
        log.debug("使用" + getClass().getSimpleName() + "比对字段" + compareRuleFieldsDto.getCompareFieldDto().getName());
        CompareResultDetailsField result = doCompare(define, compareRuleFieldsDto, compareDatas);
        log.debug("比对结果:" + result);
        return result;
    }


    /**
     * 比对
     * @param define
     * @param compareRuleFieldsDto
     * @param compareDatas
     * @return
     */
    protected abstract CompareResultDetailsField doCompare(List<CompareDefineDto> define, CompareRuleFieldsDto compareRuleFieldsDto, Map<Long, CompareDataDto> compareDatas);

}
