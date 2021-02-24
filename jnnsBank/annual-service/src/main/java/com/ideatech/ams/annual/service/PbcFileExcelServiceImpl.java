package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.PbcAccountExcelInfo;
import com.ideatech.ams.annual.util.ExcelReadConfig;
import com.ideatech.ams.annual.util.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 人行下载文件
 * @Author wanghongjie
 * @Date 2018/8/2
 **/
@Service
@Transactional
@Slf4j
public class PbcFileExcelServiceImpl implements PbcFileExcelService{
    @Override
    public List<PbcAccountExcelInfo> getPbcInfoXlsAccounts(String amsFilePath) throws Exception {
        if (StringUtils.isBlank(amsFilePath) || !new File(amsFilePath).exists()) {
            return new ArrayList<PbcAccountExcelInfo>();
        }
        return (List<PbcAccountExcelInfo>) ExcelUtils.readFile(new File(amsFilePath), PbcAccountExcelInfo.class, buildPbcExcelReadConfig());

    }

    private ExcelReadConfig buildPbcExcelReadConfig() {
        ExcelReadConfig excelReadConfig = new ExcelReadConfig();
        excelReadConfig.addMapping(0, PbcAccountExcelInfo.acctNoStr);
        excelReadConfig.addMapping(1, PbcAccountExcelInfo.bankCodeStr);
        excelReadConfig.addMapping(2, PbcAccountExcelInfo.bankNameStr);
        excelReadConfig.addMapping(3, PbcAccountExcelInfo.depositorNameStr);
        excelReadConfig.addMapping(4, PbcAccountExcelInfo.regAreaCodeStr);
        excelReadConfig.addMapping(5, PbcAccountExcelInfo.acctNameStr);
        excelReadConfig.addMapping(6, PbcAccountExcelInfo.acctTypeStr);
        excelReadConfig.addMapping(7, PbcAccountExcelInfo.accountKeyStr);
        excelReadConfig.addMapping(8, PbcAccountExcelInfo.acctOpenDateStr);
        excelReadConfig.addMapping(9, PbcAccountExcelInfo.acctCancelDateStr);
        excelReadConfig.addMapping(10, PbcAccountExcelInfo.acctStatusStr);
        excelReadConfig.addMapping(11, PbcAccountExcelInfo.currencyTypeStr);
        excelReadConfig.addMapping(12, PbcAccountExcelInfo.currencyStr);
        excelReadConfig.addMapping(13, PbcAccountExcelInfo.capitalPropertyStr);
        return excelReadConfig;
    }
}
