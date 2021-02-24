package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.ws.dto.ImageBatchInfo;
import com.ideatech.common.dto.ResultDto;

public interface PbcApiService {

    /**
     * 开户预校验
     *
     * @param accountKey
     * @param regAreaCode
     * @param organCode
     * @return
     */
    ResultDto checkPbcInfo(String accountKey, String regAreaCode, String organCode);

    /**
     * 开户预校验
     *
     * @param accountKey
     * @param regAreaCode
     * @param organCode
     * @param username
     * @return
     */
    ResultDto checkPbcInfo(String accountKey, String regAreaCode, String organCode, String username);

    /**
     * 同步人行
     *
     * @param organCode
     * @param billsPublic
     * @return
     */
    ResultDto syncPbc(String organCode, AllBillsPublicDTO billsPublic);

    /**
     * 同步分行（覆盖流水）
     *
     * @param organCode
     * @param billsPublic
     * @param needSync
     * @param reenterable
     * @return
     */
    ResultDto syncPbc(String organCode, AllBillsPublicDTO billsPublic, Boolean needSync, Boolean reenterable);

    /**
     * 同步人行
     *
     * @param organCode
     * @param billsPublic
     * @param needSync    是否需要上报
     * @return
     */
    ResultDto syncPbc(String organCode, AllBillsPublicDTO billsPublic, Boolean needSync);

    /**
     * 纯接口模式（覆盖流水）
     *
     * @param organCode
     * @param billsPublic
     * @return
     */
    ResultDto reenterablePbcSync(String organCode, AllBillsPublicDTO billsPublic);

    /**
     * 纯接口模式（覆盖流水）,配合影像接口使用
     * @param organCode
     * @param billsPublic
     * @param flag 0 代表影像接口先调用
     *              1  代表上报接口先调用
     * @return
     */
    ResultDto reenterablePbcSync(String organCode, AllBillsPublicDTO billsPublic,String flag);

    /**
     * 纯接口模式（覆盖流水）
     * 包含上报系统选择
     *
     * @param organCode
     * @param billsPublic
     * @return
     */
    ResultDto reenterablePbcSync(String organCode, AllBillsPublicDTO billsPublic, Boolean syncAms, Boolean syncEccs);


    /**
     * 报送人行接口，根据流水号
     *
     * @param billNo
     * @param validateStatus
     * @return
     */
    ResultDto submitPbc(String billNo, Boolean validateStatus);

    /**
     * 纯接口模式（覆盖流水）,包含图片影像和视频影像直接关联
     * @param organCode
     * @param billsPublic
     * @return
     */
    ResultDto reenterablePbcSync(String organCode, AllBillsPublicDTO billsPublic,ImageBatchInfo batchInfo);
}
