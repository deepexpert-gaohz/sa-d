package com.ideatech.ams.account.service;

import com.ideatech.ams.account.dao.PbcSyncListDao;
import com.ideatech.ams.account.dto.PbcSyncListDto;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.entity.PbcSyncList;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.util.BeanCopierUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class PbcSyncListServiceImpl implements PbcSyncListService {

    @Autowired
    private PbcSyncListDao pbcSyncListDao;

    @Override
    public List<PbcSyncListDto> getPbcSyncList() {
        List<PbcSyncListDto> pbcSyncListDtos = new ArrayList<PbcSyncListDto>();
        //拿到所有未核准的对象list
        List<PbcSyncList> pbcSyncLists = pbcSyncListDao.findBySyncStatus(CompanyIfType.No);
        for (PbcSyncList pbcsync : pbcSyncLists ) {
            PbcSyncListDto pbcSyncListDto = new PbcSyncListDto();
            BeanCopierUtils.copyProperties(pbcsync, pbcSyncListDto);
            pbcSyncListDtos.add(pbcSyncListDto);
        }
        return pbcSyncListDtos;
    }

	@Override
	public List<PbcSyncListDto> getUnPushSyncList() {
		List<PbcSyncListDto> syncListDtos = new ArrayList<PbcSyncListDto>();
		//拿到所有未核准的对象list
		List<PbcSyncList> pbcSyncLists = pbcSyncListDao.findByIsPush(CompanyIfType.No);
		for (PbcSyncList pbcsync : pbcSyncLists ) {
			PbcSyncListDto pbcSyncListDto = new PbcSyncListDto();
			BeanCopierUtils.copyProperties(pbcsync, pbcSyncListDto);
			syncListDtos.add(pbcSyncListDto);
		}
		return syncListDtos;
	}

    @Override
    public List<PbcSyncListDto> getCancelHeZhunList() {
        List<PbcSyncListDto> syncListDtos = new ArrayList<PbcSyncListDto>();
        //拿到所有取消核准未推送的数据
        List<PbcSyncList> pbcSyncLists = pbcSyncListDao.findByCancelHeZhunAndIsPush(Boolean.TRUE,CompanyIfType.No);
        for (PbcSyncList pbcsync : pbcSyncLists ) {
            PbcSyncListDto pbcSyncListDto = new PbcSyncListDto();
            BeanCopierUtils.copyProperties(pbcsync, pbcSyncListDto);
            syncListDtos.add(pbcSyncListDto);
        }
        return syncListDtos;
    }

    @Override
    public void savePbcSyncList(PbcSyncListDto pbcSyncListDto) {
        if (pbcSyncListDto != null) {
            PbcSyncList pbcSyncList = pbcSyncListDao.findOne(pbcSyncListDto.getId());
            if (pbcSyncList != null) {
                BeanCopierUtils.copyProperties(pbcSyncListDto, pbcSyncList);
                pbcSyncListDao.save(pbcSyncList);
            } else {
                log.error("推送核准号列表中未找到账号为{}的数据", pbcSyncListDto.getAcctNo());
            }
        }
    }

    @Override
    public void savePbcSyncList(AllBillsPublicDTO billsPublic) {
        PbcSyncList pbcSyncList = new PbcSyncList();
        pbcSyncList.setBillType(billsPublic.getBillType());
        pbcSyncList.setAcctNo(billsPublic.getAcctNo());
        pbcSyncList.setSyncStatus(CompanyIfType.No);
        pbcSyncList.setIsPush(CompanyIfType.No);
        pbcSyncList.setOrganFullId(billsPublic.getOrganFullId());
        pbcSyncList.setAccountKey(billsPublic.getAccountKey());
        pbcSyncList.setBillId(billsPublic.getId());
        pbcSyncListDao.save(pbcSyncList);
    }

    @Override
    public void saveCancelHeZhunPbcSyncList(AllBillsPublicDTO allBillsPublic) {
        PbcSyncList pbcSyncList = new PbcSyncList();
        pbcSyncList.setBillType(allBillsPublic.getBillType());
        pbcSyncList.setAcctNo(allBillsPublic.getAcctNo());
        pbcSyncList.setSyncStatus(CompanyIfType.Yes);
        pbcSyncList.setIsPush(CompanyIfType.No);
        pbcSyncList.setOrganFullId(allBillsPublic.getOrganFullId());
        pbcSyncList.setAccountKey(allBillsPublic.getAccountKey());
        pbcSyncList.setBillId(allBillsPublic.getId());
        pbcSyncList.setCancelHeZhun(allBillsPublic.getCancelHeZhun());
        pbcSyncList.setAcctCreateDate(allBillsPublic.getAcctCreateDate());
        //临时非临时增加有效日期的字段
        if(allBillsPublic.getAcctType() == CompanyAcctType.linshi || allBillsPublic.getAcctType() == CompanyAcctType.feilinshi){
            pbcSyncList.setEffectiveDate(allBillsPublic.getEffectiveDate());
        }
        pbcSyncListDao.save(pbcSyncList);
    }

    @Override
    public List<PbcSyncListDto> findByOrganFullIdLike(String organFullId) {
        return ConverterService.convertToList(pbcSyncListDao.findByOrganFullIdLike(organFullId),PbcSyncListDto.class);
    }
}
