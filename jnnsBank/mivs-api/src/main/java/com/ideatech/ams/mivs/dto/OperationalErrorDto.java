package com.ideatech.ams.mivs.dto;

import lombok.Data;

/**
 * @author jzh
 * @date 2019/7/23.
 * 报文申请拒绝传输对象
 */

@Data
public class OperationalErrorDto implements ReplyMsgDto{

    /**********************************************失败start***********************************************/

    /**
     * ProcessStatus
     * 申请报文拒绝状态
     * <ProcSts>  [1..1] ProcessCode (Max4Text) 禁止中文
     */
    private String procSts;

    /**
     * ProcessCode
     * 申请报文拒绝码
     * <ProcCd>  [1..1] Max8Text  禁止中文
     */
    private String procCd;

    /**
     * RejectInformation
     * 申请报文拒绝信息
     * <Rjctinf>  [1..1] Max105Text
     */
    private String rjctInf;

    /**********************************************失败end***********************************************/
}
