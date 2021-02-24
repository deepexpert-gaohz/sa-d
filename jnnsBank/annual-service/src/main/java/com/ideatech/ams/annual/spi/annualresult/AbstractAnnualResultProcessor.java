package com.ideatech.ams.annual.spi.annualresult;

import com.ideatech.ams.annual.dto.AnnualResultDto;
import com.ideatech.ams.annual.entity.AnnualResult;
import com.ideatech.ams.annual.spi.AnnualResultProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAnnualResultProcessor implements AnnualResultProcessor, BeanNameAware {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String beanName;

    @Override
    public Page<AnnualResultDto> query(AnnualResultDto annualResultInfo, Pageable pageable) {
        logger.info("使用" + beanName + "处理查询请求");
        pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize()/*, new Sort(Direction.ASC, "acctNo")*/);
        Page<AnnualResultDto> result = doQuery(annualResultInfo, pageable);
        logger.info("查询结果总数:" + result.getTotalElements());
        return result;
    }

    @Override
    public List<AnnualResultDto> queryAll(AnnualResultDto annualResultInfo) {
        logger.info("使用" + beanName + "处理查询请求");
        List<AnnualResultDto> result = doQueryAll(annualResultInfo);
        logger.info("查询结果总数:" + result.size());
        return result;
    }

    @Override
    public long count(AnnualResultDto annualResultInfo) {
        logger.info("使用" + beanName + "处理查询请求");
        long count = countAll(annualResultInfo);
        logger.info("查询结果总数:" + count);
        return count;
    }

    protected Page<AnnualResultDto> getPageAnnualResultInfoPage(Page<AnnualResult> pageAnnualResult, Pageable pageable) {
        List<AnnualResultDto> resultList = new ArrayList<AnnualResultDto>();
        List<AnnualResult> dataList = pageAnnualResult.getContent();
        AnnualResultDto info = null;
        for (AnnualResult annualResult : dataList) {
            info = new AnnualResultDto();
            BeanUtils.copyProperties(annualResult, info);
            info.setTaskId(annualResult.getTaskId());
            resultList.add(info);
        }
        return new PageImpl<AnnualResultDto>(resultList, pageable, pageAnnualResult.getTotalElements());
    }

    protected abstract Page<AnnualResultDto> doQuery(AnnualResultDto annualResultInfo, Pageable pageable);

    protected abstract List<AnnualResultDto> doQueryAll(AnnualResultDto annualResultInfo);

    protected abstract long countAll(AnnualResultDto annualResultInfo);

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

}
