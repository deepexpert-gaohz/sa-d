package com.ideatech.ams.service.impl;

import com.ideatech.ams.dao.SyncCompareDao;
import com.ideatech.ams.dao.spec.SyncCompareSpec;
import com.ideatech.ams.domain.SyncCompare;
import com.ideatech.ams.dto.SyncCompareInfo;
import com.ideatech.ams.service.SyncCompareService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

/**
 * 上报账户实时比对报表实现类
 *
 * @auther ideatech
 * @create 2018-11-29 9:43 AM
 **/
@Service
@EnableTransactionManagement
public class SyncCompareServiceImpl implements SyncCompareService {

    private static final Logger log = LoggerFactory.getLogger(SyncCompareServiceImpl.class);

    @Autowired
    private SyncCompareDao syncCompareDao;

    @Override
    public TableResultResponse<SyncCompareInfo> query(SyncCompareInfo info, Pageable pageable) {
        // 2018-122-06 获取用户所在结构fullid
        String orgnafullid = SecurityUtils.getCurrentOrgFullId();
        if(null==orgnafullid){
            info.setOrganFullId("1");
        }else {
            info.setOrganFullId(orgnafullid);
        }
        log.info("查询的用户fullId："+info.getOrganFullId());
        long count=syncCompareDao.count(new SyncCompareSpec(info));
        Page<SyncCompare> page =syncCompareDao.findAll(new SyncCompareSpec(info),pageable);
        List<SyncCompare> list = page.getContent();
        log.info("页面获取到的数据为："+list.toString());
        List<SyncCompareInfo> listDto = ConverterService.convertToList(list, SyncCompareInfo.class);
        return new TableResultResponse<SyncCompareInfo>((int) count, listDto);
    }

    /**
     * 保存账户数据
     *
     * @param syncCompareInfo
     */
    @Override
    public void create(SyncCompareInfo syncCompareInfo) {
        SyncCompare syncCompare = ConverterService.convert(syncCompareInfo, SyncCompare.class);
        syncCompare.setId(null);
        syncCompareDao.save(syncCompare);
        syncCompareInfo.setId(syncCompare.getId());
    }

    @Override
    public void update(SyncCompareInfo syncCompareInfo) {
        SyncCompare syncCompare = syncCompareDao.getOne(syncCompareInfo.getId());
        ConverterService.convert(syncCompareInfo, syncCompare);
        syncCompareDao.save(syncCompare);
    }

    @Override
    public void delete(Long id) {
        syncCompareDao.delete(id);
    }

    @Override
    public SyncCompare findTopByAcctNoAndKaixhubzAndBusinessDateOrderByLastUpdateDateDesc(String acctNo, String kaixhubz, String BusinessDate) {
        SyncCompare syncCompare = syncCompareDao.findTopByAcctNoAndKaixhubzAndBusinessDateOrderByLastUpdateDateDesc(acctNo,kaixhubz,BusinessDate);
        return syncCompare;
    }
}
