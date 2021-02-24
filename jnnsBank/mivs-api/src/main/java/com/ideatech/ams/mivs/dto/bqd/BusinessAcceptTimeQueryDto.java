package com.ideatech.ams.mivs.dto.bqd;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author jzh
 * @date 2019/7/18.
 * 企业信息联网核查业务受理时间查询报文传输对象
 */

@Data
public class BusinessAcceptTimeQueryDto {

    private Long id;

    /**
     * SystemIndicator
     * 核查系统标识
     * <SysInd>  [1..1] SystemTypeCode (Max4Text)
     */
    @NotBlank(message="核查系统标识")
    @Length(max = 4, message = "核查系统标识超过4")
    private String sysInd;

    /**
     * QueryDate
     * 查询日期
     * <QueDt>  [1..1]  ISODate  禁止中文
     */
    @NotBlank(message="查询日期")
    private String queDt;
}
