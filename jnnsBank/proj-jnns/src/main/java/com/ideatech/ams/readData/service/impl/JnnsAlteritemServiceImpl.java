package com.ideatech.ams.readData.service.impl;

import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.dao.HeGuiYuJingAllDao;
import com.ideatech.ams.account.dao.bill.AccountBillsAllDao;
import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.entity.HeGuiYuJingAll;
import com.ideatech.ams.readData.AlteritemMointor;
import com.ideatech.ams.readData.AlteritemMointorInfo;
import com.ideatech.ams.readData.AlteritemMointorSpec;
import com.ideatech.ams.readData.dao.JnnsAlteritemDao;
import com.ideatech.ams.readData.service.JnnsAlteritemService;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j
public class JnnsAlteritemServiceImpl implements JnnsAlteritemService {

    @Autowired
    private JnnsAlteritemDao jnnsAlteritemDao;

    @Autowired
    private AccountBillsAllDao accountBillsAllDao;

    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private HeGuiYuJingAllDao heGuiYuJingAllDao;

    @Autowired
    private AccountsAllDao accountsAllDao;

    @Override
    public TableResultResponse<AlteritemMointor> query(AlteritemMointor amt, Pageable pageable) {
        //用户对应的机构号
        String fullId = SecurityUtils.getCurrentOrgFullId();

        //先给fullid赋值，再查询
        Page<AlteritemMointorInfo> page1 = jnnsAlteritemDao.findAll(new AlteritemMointorSpec(amt), pageable);
        List<AlteritemMointorInfo> list1 = page1.getContent();
        for (AlteritemMointorInfo alteritemMointorInfo : list1
        ) {
            if (StringUtils.isBlank(alteritemMointorInfo.getOrganFullId())) {
                List<AccountsAll> accountsAlls = accountsAllDao.findByCustomerNo(alteritemMointorInfo.getCustomerId());

                if (accountsAlls.size() > 0 && StringUtils.isNotBlank(accountsAlls.get(0).getOrganFullId())) {
                    String code = organizationDao.findCodeByOrgFullId(accountsAlls.get(0).getOrganFullId());
                    jnnsAlteritemDao.update(alteritemMointorInfo.getCustomerId(), accountsAlls.get(0).getOrganFullId(), code);

                }
            }

        }
        //添加查询条件（超级管理员除外）
        if(fullId != "1" && !("1").equals(fullId)) {

             amt.setOrganFullId(fullId);
        }
        long count=jnnsAlteritemDao.count(new AlteritemMointorSpec(amt));
        Page<AlteritemMointorInfo> page =jnnsAlteritemDao.findAll(new AlteritemMointorSpec(amt),pageable);
        List<AlteritemMointorInfo> list = page.getContent();

        log.info("页面获取到的数据为："+list.toString());
        List<AlteritemMointor> listDto = ConverterService.convertToList(list, AlteritemMointor.class);
        return new TableResultResponse<AlteritemMointor>((int) count, listDto);
    }
    @Override
    public TableResultResponse<AlteritemMointor> query1(AlteritemMointor amt, Pageable pageable) {
        String fullId = "1";
        amt.setCustomerId("");
        Page<AlteritemMointorInfo> page1 =jnnsAlteritemDao.findAll(new AlteritemMointorSpec(amt),pageable);
        List<AlteritemMointorInfo> list1 = page1.getContent();
        for (AlteritemMointorInfo alteritemMointorInfo:list1
                ) {
            if(StringUtils.isBlank(alteritemMointorInfo.getOrganFullId())) {
                List<AccountsAll> accountsAlls = accountsAllDao.findByCustomerNo(alteritemMointorInfo.getCustomerId());
                if (accountsAlls.size() > 0 && StringUtils.isNotBlank(accountsAlls.get(0).getOrganFullId())) {
                    String code =  organizationDao.findCodeByOrgFullId(accountsAlls.get(0).getOrganFullId());
                    jnnsAlteritemDao.update(alteritemMointorInfo.getCustomerId(), accountsAlls.get(0).getOrganFullId(),code);
                }
            }
        }
        if(fullId != "1" && !("1").equals(fullId)) {
            amt.setOrganFullId(fullId);
        }
        long count=jnnsAlteritemDao.count(new AlteritemMointorSpec(amt));
        List<AlteritemMointorInfo> all = jnnsAlteritemDao.findAll();
        if(all.size()>0){
            List<AlteritemMointor> listDto = ConverterService.convertToList(all, AlteritemMointor.class);
            heGuiYuJingAllDao.deleteAllByYuJingType("7");
            log.info("变更项报存开始----");
        if(listDto!=null && listDto.size() > 0){
            for(AlteritemMointor    dtoo : listDto){
                HeGuiYuJingAll heGuiYuJingAll=new HeGuiYuJingAll();
                if(StringUtils.isNotBlank(dtoo.getCustomerId())){
                    heGuiYuJingAll.setCustomerId(dtoo.getCustomerId());
                }
                if(StringUtils.isNotBlank(dtoo.getAlterItem())){
                    heGuiYuJingAll.setAlterItem(dtoo.getAlterItem());
                }
                if(StringUtils.isNotBlank(dtoo.getAlterBefore())){
                    heGuiYuJingAll.setAlterBefore(dtoo.getAlterBefore());
                }
                if(StringUtils.isNotBlank(dtoo.getAlterAfter())){
                    heGuiYuJingAll.setAlterAfter(dtoo.getAlterAfter());
                }
                if(StringUtils.isNotBlank(dtoo.getWarnTime())){
                    heGuiYuJingAll.setWarnTime(dtoo.getWarnTime());
                }
                if(StringUtils.isNotBlank(dtoo.getDataDt())){
                    heGuiYuJingAll.setDataDt(dtoo.getDataDt());
                }
                if(StringUtils.isNotBlank(dtoo.getEtlDate())){
                    heGuiYuJingAll.setEtlDate(dtoo.getEtlDate());
                }
                heGuiYuJingAll.setYuJingType("7");
                heGuiYuJingAllDao.save(heGuiYuJingAll);
                }
            }
        }
        log.info("保存全量预警数据变更项-----");
        Page<AlteritemMointorInfo> page =jnnsAlteritemDao.findAll(new AlteritemMointorSpec(amt),pageable);
        List<AlteritemMointorInfo> list = page.getContent();
        List<AlteritemMointor> listDto = ConverterService.convertToList(list, AlteritemMointor.class);
        return new TableResultResponse<AlteritemMointor>((int) count, listDto);
    }
}
