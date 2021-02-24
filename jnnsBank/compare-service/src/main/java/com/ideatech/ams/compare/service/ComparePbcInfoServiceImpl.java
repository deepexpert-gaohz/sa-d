package com.ideatech.ams.compare.service;

import com.ideatech.ams.compare.dao.ComparePbcInfoDao;
import com.ideatech.ams.compare.dto.ComparePbcInfoDto;
import com.ideatech.ams.compare.entity.ComparePbcInfo;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description 人行数据实现层
 * @Author wanghongjie
 * @Date 2019/2/15
 **/
@Service
@Slf4j
@Transactional
public class ComparePbcInfoServiceImpl extends BaseServiceImpl<ComparePbcInfoDao, ComparePbcInfo, ComparePbcInfoDto> implements ComparePbcInfoService{
    @Value("${compare.online.pbc.validDays}")
    private int realTimeValidDays;

    @Override
    public void saveComparePbcInfo(ComparePbcInfoDto comparePbcInfoDto) {
        ComparePbcInfo comparePbcInfo = null;
        if(comparePbcInfoDto.getId() != null){
            comparePbcInfo = getBaseDao().findById(comparePbcInfoDto.getId());
            BeanUtils.copyProperties(comparePbcInfoDto,comparePbcInfo);
        }else{
            comparePbcInfo = ConverterService.convert(comparePbcInfoDto, ComparePbcInfo.class);
        }
        getBaseDao().save(comparePbcInfo);
        comparePbcInfoDto.setId(comparePbcInfo.getId());
    }

    @Override
    public ComparePbcInfoDto getComparePbcInfoBaseLocal(String acctNo) {
        ComparePbcInfo comparePbcInfo = getBaseDao().findFirstByAcctNoOrderByCreatedDateDesc(acctNo);
        //返回的信息
        if (comparePbcInfo == null) {
            log.info("本地没有[{}]人行数据", acctNo);
        }
        if (comparePbcInfo != null && isInValidDays(comparePbcInfo)) {
            log.info("本地存在有效期内的[{}]人行数据", acctNo);
            return ConverterService.convert(comparePbcInfo,ComparePbcInfoDto.class);
        }
        return null;
    }

    /**
     *
     * @param comparePbcInfo
     * @return
     */
    protected boolean isInValidDays(ComparePbcInfo comparePbcInfo) {
        //计算有效期
        DateTime now = new DateTime();
        DateTime updateTime = new DateTime(comparePbcInfo.getCreatedDate());
        int days = Days.daysBetween(updateTime, now).getDays();
        if (days >= realTimeValidDays) {
            log.info("本地数据失效,天数{}", days);
            return false;
        } else {
            log.info("本地数据还在有效期内,天数{}", days);
            return true;
        }
    }
}
