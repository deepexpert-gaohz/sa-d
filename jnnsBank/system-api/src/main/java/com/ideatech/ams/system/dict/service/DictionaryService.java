package com.ideatech.ams.system.dict.service;

import com.ideatech.ams.system.dict.dto.DictionaryDto;
import com.ideatech.ams.system.dict.dto.OptionDto;

import java.util.List;

public interface DictionaryService {
    void save(DictionaryDto dictionaryDto);

    void deleteDictionary(Long id);

    List<DictionaryDto> listWithoutCascade();

    List<OptionDto> listOptionByDictId(Long id);

    List<OptionDto> findOptionsByDictionaryName(String name);

    List<OptionDto> findOptionsByDictionaryNameStartWith(String name);

    void saveOption(OptionDto optionDto);

    void deleteOption(Long optionId);

    OptionDto findOptionById(Long optionId);

	/**
	 * 字典转换
	 * 如果没有对应值则反回原值
	 * @param dictionaryName
	 * @param optionName
	 * @return
	 */
    String transalte(String dictionaryName, String optionName);

    /**
     * 字典不存在返回原值
     * 如果为空，再次查询是否已经转换过，如果转换过则直接返回原值
     * 否则返回空
     * @param dictionaryName
     * @param optionName
     * @return
     */
    String transalteNull2Empty(String dictionaryName, String optionName);

    /**
     * 字典转换
     * optionName模糊查询
     * @param dictionaryName
     * @param optionName
     * @return
     */
    String transalteLike(String dictionaryName, String optionName);

    DictionaryDto findDictionaryById(Long id);
	
	List<DictionaryDto> getDictionaryLikeName(String name);
	
	List<OptionDto> listOptionByDictName(String name);

	String getNameByValue(String dictionaryName, String optionValue);
}
