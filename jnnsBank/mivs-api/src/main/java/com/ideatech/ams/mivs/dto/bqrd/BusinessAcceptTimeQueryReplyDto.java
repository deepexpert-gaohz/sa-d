package com.ideatech.ams.mivs.dto.bqrd;


import com.ideatech.ams.mivs.dto.ReplyMsgDto;
import lombok.Data;

/**
 * @author jzh
 * @date 2019/7/18.
 * 企业信息联网核查业务受理时间查询应答报文传输对象
 */
@Data
public class BusinessAcceptTimeQueryReplyDto implements ReplyMsgDto {

    /**
     * OrginalQueryDate
     * 原查询日期
     * <OrgnlQueDt>  [1..1]  ISODate  禁止中文
     */
    private String orgnlQueDt;

    /**
     * ProcessStatus
     * 申请报文处理状态
     * <ProcSts>  [1..1]  ProcessCode
     * PR07 已处理
     * PR09 已拒绝
     */
    private String procSts;

    /**
     * ProcessCode
     * 申请报文处理码
     * <ProcCd>  [0..1]  Max8Text
     */
    private String procCd;

    /**
     * RejectInformation
     * 申请报文拒绝信息
     * <RjctInf>  [0..1]  Max105Text
     * 当申请报文处理状态为 PR09 已拒绝时填写
     */
    private String rjctInf;

    /**
     * ServiceInformation
     * <SvcInf>  [0..1]
     * 当被查询日期有效且申请报文处理状态不为 PR09 时填写
     */
    private ServiceInformationDto svcInf;
}
