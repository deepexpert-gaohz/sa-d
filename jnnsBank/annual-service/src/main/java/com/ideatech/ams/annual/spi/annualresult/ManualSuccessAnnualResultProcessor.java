package com.ideatech.ams.annual.spi.annualresult;

import com.ideatech.ams.annual.dao.AnnualResultDao;
import com.ideatech.ams.annual.dao.spec.AnnualResultSpec;
import com.ideatech.ams.annual.dto.AnnualResultDto;
import com.ideatech.ams.annual.entity.AnnualResult;
import com.ideatech.ams.annual.enums.ForceStatusEnum;
import com.ideatech.ams.annual.enums.PbcSubmitStatusEnum;
import com.ideatech.common.converter.ConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("manualSuccessAnnualResultProcessor")
public class ManualSuccessAnnualResultProcessor extends AbstractAnnualResultProcessor {

    @Autowired
    private AnnualResultDao annualResultDao;

    /**
     * 手工处理成功列表
     * @param annualResultInfo
     * @param pageable
     * @return
     */
    @Override
    protected Page<AnnualResultDto> doQuery(AnnualResultDto annualResultInfo, Pageable pageable) {
        this.setParameter(annualResultInfo);//设置默认查询参数
        Page<AnnualResult> annualResults = annualResultDao.findAll(new AnnualResultSpec(annualResultInfo), pageable);

        return getPageAnnualResultInfoPage(annualResults, pageable);
    }

    @Override
    protected List<AnnualResultDto> doQueryAll(AnnualResultDto annualResultInfo) {
        this.setParameter(annualResultInfo);//设置默认查询参数
        List<AnnualResult> annualResults = annualResultDao.findAll(new AnnualResultSpec(annualResultInfo));
        return ConverterService.convertToList(annualResults, AnnualResultDto.class);
    }

    @Override
    protected long countAll(AnnualResultDto annualResultInfo) {
        this.setParameter(annualResultInfo);//设置默认查询参数
        long count = annualResultDao.count(new AnnualResultSpec(annualResultInfo));
        return count;
    }

    /**
     * 设置默认查询参数
     */
    private void setParameter(AnnualResultDto annualResultInfo) {
        annualResultInfo.setForceStatus(null);
        List<ForceStatusEnum> list = new ArrayList<>();
        list.add(ForceStatusEnum.SUCCESS);
        list.add(ForceStatusEnum.AGAIN_SUCCESS);
        annualResultInfo.setForceStatusList(list);
        annualResultInfo.setDeleted(false);
    }

	@Override
	public String getName() {
		return "手动年检成功清单";
	}
}
