package com.ideatech.ams.mivs.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.MappedSuperclass;

/**
 * @author jzh
 * @date 2019/7/25.
 * 公共扩展对象类
 */

@Data
@MappedSuperclass
public class ExtendEntity extends BaseMaintainablePo {


    /**
     * 申请报文标识号
     */
    private String orgnlDlvrgMsgId;

    /**
     * 应答报文标识号
     */
    private String orgnlRcvgMsgId;

    /**
     * 机构fullId
     */
    private String organFullId;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构代码
     */
    private String code;

    /**
     * 操作员
     */
    private String createdUserName;

    /**
     * 操作时间
     */
    private String createTime;

    /**
     * 是否返回应答报文标记
     */
    private Boolean flag;

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

    /**
     * 扩展字段
     */
    private String String001;
    private String String002;
    private String String003;
    private String String004;
    private String String005;
    private String String006;
    private String String007;
    private String String008;
    private String String009;
    private String String010;
    private String String011;
    private String String012;
    private String String013;
    private String String014;
    private String String015;

}
