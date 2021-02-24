package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.compare.service.CompareDataConvertService;
import com.ideatech.ams.system.dict.dto.OptionDto;
import com.ideatech.ams.system.dict.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author jzh
 * @date 2019-08-27.
 */

@Slf4j
public class DefaultCompareDataConvertServiceImpl implements CompareDataConvertService {


    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 是否初始化
     */
    private boolean flag = false;

    /**
     * pbc转中文，使用ValueItem或者
     */
    private Map<String, String> industryCodeDicMap = new HashMap<>();

    private Map<String, String> depositorTypeDicMap = new HashMap<>();

    private Map<String, String> fileTypeDicMap = new HashMap<>();

    private Map<String, String> acctTypeDicMap = new HashMap<>();

    private Map<String, String> accountStatusDicMap = new HashMap<>();

    private Map<String, String> legalIdcardTypeDicMap = new HashMap<>();


    /**
     * 参考 CoreFileSaveExecutor 转换
     * @param compareDataDto
     */
    @Override
    public void dataTransformation(CompareDataDto compareDataDto) {

        if (!flag){
            readyDicMap();
            flag = true;
        }

        //行业归属
        if (industryCodeDicMap.containsKey(compareDataDto.getIndustryCode())){
            compareDataDto.setIndustryCode(industryCodeDicMap.get(compareDataDto.getIndustryCode()));
        }
        //存款人类别
        if (depositorTypeDicMap.containsKey(compareDataDto.getDepositorType())){
            compareDataDto.setDepositorType(depositorTypeDicMap.get(compareDataDto.getDepositorType()));
        }
        //证明文件种类
        if (fileTypeDicMap.containsKey(compareDataDto.getFileType())){
            compareDataDto.setFileType(fileTypeDicMap.get(compareDataDto.getFileType()));
        }
        //账户性质
        if (acctTypeDicMap.containsKey(compareDataDto.getAcctType())){
            compareDataDto.setAcctType(acctTypeDicMap.get(compareDataDto.getAcctType()));
        }
        //账户状态
        if (accountStatusDicMap.containsKey(compareDataDto.getAccountStatus())){
            compareDataDto.setAccountStatus(accountStatusDicMap.get(compareDataDto.getAccountStatus()));
        }

        //证件种类
        if (legalIdcardTypeDicMap.containsKey(compareDataDto.getLegalIdcardType())){
            compareDataDto.setLegalIdcardType(legalIdcardTypeDicMap.get(compareDataDto.getLegalIdcardType()));
        }
    }

    /**
     * 刷新转换的字典
     * 每次批量保存compareData前记得调用一下。
     */
    @Override
    public void update() {
        flag = false;
    }


    /**
     * 准备各个字典项
     */
    private void readyDicMap(){
        //清除字典转换数据
        clearDicMap();

        //行业归属 industryCodeValueItem不行(A->农、林、牧、渔业)，只能使用行业归属字典反向  A$$农、林、牧、渔业$$第一产业 $$1--->A：农、林、牧、渔业
        //因为core2pbc   A--->A$$农、林、牧、渔业$$第一产业 $$1
        List<OptionDto> industryCode = dictionaryService.findOptionsByDictionaryName("行业归属");
        for (OptionDto option : industryCode) {
            industryCodeDicMap.put(option.getValue(), option.getName());
        }

        //存款人类别 depositorTypeValue2Item
        List<OptionDto> depositorType = dictionaryService.findOptionsByDictionaryName("depositorTypeValue2Item");
        for (OptionDto option : depositorType) {
            depositorTypeDicMap.put(option.getName(), option.getValue());
        }

        //证明文件种类 fileType..
        List<OptionDto> fileType = dictionaryService.findOptionsByDictionaryNameStartWith("fileType");
        for (OptionDto option : fileType) {
            fileTypeDicMap.put(option.getName(), option.getValue());
        }

        //账户性质 目前没有对应valueItem，使用账户性质字典反向
        List<OptionDto> acctType = dictionaryService.findOptionsByDictionaryName("账户性质");
        for (OptionDto option : acctType) {
            acctTypeDicMap.put(option.getValue(), option.getName());
        }

        //账户状态 目前没有对应valueItem，使用账户状态字典反向
        List<OptionDto> accountStatus = dictionaryService.findOptionsByDictionaryName("账户状态");
        for (OptionDto option : accountStatus) {
            accountStatusDicMap.put(option.getValue(), option.getName());
        }

        //证件种类 legalIdcardTypeValue2Item
        List<OptionDto> legalIdcardType = dictionaryService.findOptionsByDictionaryName("legalIdcardTypeValue2Item");
        for (OptionDto option : legalIdcardType) {
            legalIdcardTypeDicMap.put(option.getName(), option.getValue());
        }
    }

    /**
     * 清除字典信息
     */
    private void clearDicMap() {
        industryCodeDicMap.clear();
        depositorTypeDicMap.clear();
        fileTypeDicMap.clear();
        acctTypeDicMap.clear();
        accountStatusDicMap.clear();
        legalIdcardTypeDicMap.clear();
    }
}
