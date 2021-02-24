package com.ideatech.ams.account.service;

import com.ideatech.ams.account.dto.PbcSyncListDto;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;

import java.util.List;

public interface PbcSyncListService {

    /**
     * 拿到人行同步对象
     * @return
     */
    List<PbcSyncListDto> getPbcSyncList();

	List<PbcSyncListDto> getUnPushSyncList();

    /**
     * 拿到取消核准推送失败数据
     */
    List<PbcSyncListDto> getCancelHeZhunList();

    /**
     * 保存PbcSyncList
     * @param pbcSyncListDto
     */
    void savePbcSyncList(PbcSyncListDto pbcSyncListDto);

	void savePbcSyncList(AllBillsPublicDTO allBillsPublic);

    /**
     * 保存取消核准推送失败数据
     * @param allBillsPublic
     */
    void saveCancelHeZhunPbcSyncList(AllBillsPublicDTO allBillsPublic);

	List<PbcSyncListDto> findByOrganFullIdLike(String organFullId);
}
