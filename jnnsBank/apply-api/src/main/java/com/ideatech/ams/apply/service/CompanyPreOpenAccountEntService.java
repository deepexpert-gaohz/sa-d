package com.ideatech.ams.apply.service;

import com.ideatech.ams.apply.cryptography.*;
import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntDto;
import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntSaicDto;
import com.ideatech.ams.apply.dto.RSACode;
import com.ideatech.ams.apply.enums.ApplyEnum;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface CompanyPreOpenAccountEntService {

	String saveApply(CompanyPreOpenAccountEntDto dto);

	TableResultResponse<CompanyPreOpenAccountEntDto> query(CompanyPreOpenAccountEntDto dto, Pageable pageable);

	Object getCount(CompanyPreOpenAccountEntDto dto);

    List<CompanyPreOpenAccountEntDto> query(CompanyPreOpenAccountEntDto dto);

	ObjectRestResponse<CompanyPreOpenAccountEntDto> findOne(String applyId);

	CompanyPreOpenAccountEntDto findByApplyid(String applyId);

    void edit(CompanyPreOpenAccountEntDto ent);

    void update(CompanyPreOpenAccountEntDto entDto);

    CompanyPreOpenAccountEntDto selectOne(Long id);

    void getApplyRecordByLastDubiousApplyIds();

    String getApplyRecordByPullId(Long maxId);

    Long getMaxId();

    String send(CryptoInput cryptoInput, CryptoOutput cryptoOutput, String url);
    
    void insertBatch(List<CryptoPullVo> list);

    void insertBatchNew(List<CryptoPullNewVo> list, String operateType);

    void sendModifyStatus(Long uuid, String status) throws Exception;

    /**
     *
     * @param applyId 预约编号
     * @param status 预约状态
     * @param pbcCode 预约开户行网点银行12位人行联行号
     * @param note 回执信息（回退回执、失败回执）
     * @throws Exception
     */
    void sendModifyStatus(String applyId, ApplyEnum status, String pbcCode, String note) throws Exception;

    /**
     * 预约单创建
     * @param applyAcctVo
     */
    String pushApplyAddAcct(CryptoAddApplyAcctVo applyAcctVo) throws Exception;

    /**
     * 预约单修改
     * @param applyAcctVo
     */
    void pushApplyEditAcct(CryptoEditApplyAcctVo applyAcctVo) throws Exception;

    /**
     * 基本户开户许可证推送
     * @param acctKeyMessage
     * @throws Exception
     */
    void pushApplyAcctKey(CryptoModifyAcctKeyMessage acctKeyMessage) throws Exception;

    String getRSACodeOrganId(RSACode rsaCode);

    void edit(CompanyPreOpenAccountEntDto entDto, CompanyPreOpenAccountEntSaicDto saicDto);
    
    CompanyPreOpenAccountEntSaicDto selectOneSaic(String applyId);

    void saveMessage(CompanyPreOpenAccountEntDto temp);

    /**
     * @param applyId  预约编号
     * @param acctOpenStatus  状态
     */
    void editModifyStatus(String applyId, String acctOpenStatus);

    /**
     *  状态更新
     * @param entDto
     * @param status
     */
    void stateUpdate(CompanyPreOpenAccountEntDto entDto, String status);

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    String getApplyRecordByPullTime(String startDateTime, String endDateTime, String operateType);

    /**
     *  预约影像接口
     * @param openAcctTransNo
     * @param reqPageNum
     * @param transNoType
     */
    Integer getApplyImagesByApplyIdAndPageNum(String openAcctTransNo, Integer reqPageNum, String transNoType);

    /**
     * 预约的影像资料判断/比对/保存
     * @param response
     */
    void doApplyImages(CryptoImagesVo response);

    /**
     * 预约影像资料的保存
     * @param response
     */
    void insertImages(CryptoImagesVo response);
    /**
     * 发送短信
     * @param id
     */
    Boolean sendMessage(Long id);

    List<CompanyPreOpenAccountEntDto> getBreakAppointInfo();

    Long countUnprocessedCountBefore(Date createdBefore, String organFullId);

    PagingDto<CompanyPreOpenAccountEntDto> listUnprocessedCountBefore(CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto,Date createdBefore, String organFullId, PagingDto pagingDto);

    /**
     * 获取前10条的预约记录
     * @param hasocr
     */
    List<CompanyPreOpenAccountEntDto> findTop10ByHasocr(String hasocr);

    /**
     * 更新hasocr为2
     */
    void updateHasocr(Long id);

    void saveApply(CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto, String bankCode);

    void saveApplyImmediately(CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto, OrganizationDto organizationDto);

    /**
     * 根据applyid获取预约状态
     */
    String getStatusByApplyid(String applyid);

    long getCompanyPreCount(CompanyPreOpenAccountEntDto dto);

    ResultDto deletePreOpenAccount(String applyid, String name);

    CompanyPreOpenAccountEntSaicDto conversion(CompanyPreOpenAccountEntSaicDto info1);

    /**
     * 根据预约编号跟企业名称查找预约数据
     * @param applyId
     * @param name
     * @return
     */
    CompanyPreOpenAccountEntDto findByApplyIdAndName(String applyId,String name);

    /**
     * 预约新增接口校验
     * @param applyAcctVo
     */
    void applyOpenValidater(CryptoAddApplyAcctVo applyAcctVo) throws Exception;

    /**
     * 预约修改接口校验
     * @param applyAcctVo
     */
    void applyEditValidater(CryptoEditApplyAcctVo applyAcctVo) throws Exception;


//    /**
//     * 根据预约编号跟企业名称去查询预约数据以及状态
//     * @param applyId
//     * @param companyName
//     * @return
//     */
//    CompanyPreOpenAccountEntDto queryApplyIdAndName(String applyId,String companyName);

}
