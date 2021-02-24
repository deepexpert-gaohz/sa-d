package com.ideatech.ams.annual.spi.annualresult;

import com.ideatech.ams.annual.dao.AnnualResultDao;
import com.ideatech.ams.annual.dao.spec.AnnualResultSpec;
import com.ideatech.ams.annual.dto.AnnualResultDto;
import com.ideatech.ams.annual.entity.AnnualResult;
import com.ideatech.ams.annual.enums.ForceStatusEnum;
import com.ideatech.ams.annual.enums.PbcSubmitStatusEnum;
import com.ideatech.ams.annual.enums.ResultStatusEnum;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.SaicStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("waitingProcessAnnualResultProcessor")
public class WaitingProcessAnnualResultProcessor extends AbstractAnnualResultProcessor {

    @Autowired
    private AnnualResultDao annualResultDao;

	/**
	 * 年检待处理列表
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
//		annualResultInfo.setForceStatus(ForceStatusEnum.SUCCESS);
		annualResultInfo.setResult(ResultStatusEnum.FAIL);
		annualResultInfo.setForceStatus(ForceStatusEnum.INIT);
//		annualResultInfo.setDeleted(false);

		List<PbcSubmitStatusEnum> strs = new ArrayList<>();
		strs.add(PbcSubmitStatusEnum.SUCCESS);
		strs.add(PbcSubmitStatusEnum.NO_NEED_SUBMIT);
		strs.add(PbcSubmitStatusEnum.WAIT_SUBMIT);

		annualResultInfo.setPbcSubmitStatusesAndNull(strs);
		List<SaicStatusEnum> saicStatuses = annualResultInfo.getSaicStatuses();
		if(saicStatuses != null && saicStatuses.size() ==1 && saicStatuses.get(0) == SaicStatusEnum.NOTFOUND){
			saicStatuses.add(null);
		}
	}

	@Override
	public String getName() {
		return "待处理年检清单";
	}
}
