package com.ideatech.ams.system.dict.dao;

import com.ideatech.ams.system.dict.entity.OptionPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OptionDao extends JpaRepository<OptionPo, Long>, JpaSpecificationExecutor<OptionPo> {
    List<OptionPo> findByDictionaryIdOrderByName(Long dictionaryId);

    List<OptionPo> findByDictionaryIdInOrderByName(List<Long> dictionaryIdList);

    OptionPo findByDictionaryIdAndName(Long dictionaryId, String name);

    OptionPo findByDictionaryIdAndValue(Long dictionaryId, String value);

    //    OptionPo findByDictionaryIdAndNameEndingWith(Long dictionaryId, String name);
    List<OptionPo> findByDictionaryIdAndNameEndingWith(Long dictionaryId, String name);

    List<OptionPo> findByDictionaryId(Long dictionaryId);
}
