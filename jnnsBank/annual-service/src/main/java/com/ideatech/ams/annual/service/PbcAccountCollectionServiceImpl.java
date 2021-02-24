package com.ideatech.ams.annual.service;

import com.ideatech.ams.pbc.dto.AmsDownTask;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

/**
 * @Description 人行账号下载服务层
 * @Author wanghongjie
 * @Date 2018/8/8
 **/
@Service
@Transactional
@Slf4j
public class PbcAccountCollectionServiceImpl implements PbcAccountCollectionService{

    @Value("${ams.export-excel.folder}")
    private String amsFilePath;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private AmsMainService amsMainService;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Override
    public String downRHAccount(long organId) throws Exception {
        return downRHAccount(organId, null, null);
    }

    @Override
    public String downRHAccount(long organId, String startdate) throws Exception {
        return downRHAccount(organId, startdate, null);
    }

    @Override
    public String downRHAccount(long organId, String startdate, String endDate) throws Exception {
        OrganizationDto organizationDto = organizationService.findById(organId);
        if (organizationDto == null) {
            throw new SyncException("机构不能为空");
        }
        if (StringUtils.isBlank(organizationDto.getName())) {
            throw new SyncException("未找到对应机构");
        }
        AmsDownTask amsDownTask = getAmsDownTask(organizationDto.getPbcCode(), startdate, endDate);
        try {
            PbcAccountDto pbcAccountDto = pbcAccountService.getPbcAccountByOrganId(organizationDto.getId(), EAccountType.AMS);
            if(pbcAccountDto == null){
                log.info("采集机构{}人行账号不可用!",organizationDto.getName());
                throw new SyncException("采集机构【"+organizationDto.getName()+"】人行账号不可用!");
            }
            amsMainService.downRHAccount(getPbcUserAccount(pbcAccountDto), amsDownTask);
        } catch (Exception e) {
            log.error("下载 [" + organizationDto.getPbcCode() + "] excel异常", e);
            throw e;
        }
        return amsDownTask.getFolderPath() + File.separator + organizationDto.getPbcCode() + ".xls";
    }


    /**
     * 获取人行下载excel任务对象
     *
     * @return
     */
    private AmsDownTask getAmsDownTask(String bankCode, String startDate, String endDate) {
        // 下载起止时间
        if (StringUtils.isBlank(startDate)) {
            startDate = "1900-01-01";
        }
        // 结束时间
        if (StringUtils.isBlank(endDate)) {
            endDate = DateUtils.getNowDateShort();
        }
        // 下载路径
        String exportFolder = amsFilePath + File.separator + startDate + "~" + endDate;

        AmsDownTask amsDownTask = new AmsDownTask();
        amsDownTask.setBeginDay(startDate);
        amsDownTask.setBankId(bankCode);
        amsDownTask.setEndDay(endDate);
        amsDownTask.setFolderPath(exportFolder);
        return amsDownTask;
    }

    private PbcUserAccount getPbcUserAccount(PbcAccountDto pbcAccountDto){
        PbcUserAccount account = new PbcUserAccount();
        account.setLoginUserName(pbcAccountDto.getAccount());
        account.setLoginPassWord(pbcAccountDto.getPassword());
        account.setLoginIp(pbcAccountDto.getIp());
        return account;
    }
}
