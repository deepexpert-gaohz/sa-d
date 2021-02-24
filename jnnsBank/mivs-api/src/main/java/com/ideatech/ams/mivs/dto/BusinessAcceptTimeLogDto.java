package com.ideatech.ams.mivs.dto;

import com.ideatech.ams.mivs.dto.bqrd.ServiceInformationDto;
import lombok.Data;

/**
 * @author jzh
 * @date 2019/7/30.
 */

@Data
public class BusinessAcceptTimeLogDto extends ExtendDto{

    /**
     * SystemIndicator
     * 核查系统标识
     * <SysInd>  [1..1] SystemTypeCode (Max4Text)
     */
    private String sysInd;

    /**
     * QueryDate
     * 查询日期
     * <QueDt>  [1..1]  ISODate  禁止中文
     */
    private String queDt;

    /**
     * OrginalQueryDate
     * 原查询日期
     * <OrgnlQueDt>  [1..1]  ISODate  禁止中文
     */
    private String orgnlQueDt;

    /**
     * ServiceInformation
     * <SvcInf>  [0..1]
     * 当被查询日期有效且申请报文处理状态不为 PR09 时填写
     */
    private ServiceInformationDto svcInf;
}
