package com.ideatech.ams.mivs.dto.bnd;

import com.ideatech.ams.mivs.dto.MessageHeaderDto;
import lombok.Data;

import java.util.List;

/**
 * @author jzh
 * @date 2019-09-11.
 */


@Data
public class BusinessAcceptTimeNoticeDto extends MessageHeaderDto {

    private Long id;

    /**
     * CurrentSystemDate
     * 系统当前日期
     * <CurSysDt>  [1..1]  ISODate  禁止中文
     */
    private String curSysDt;


    /**
     * NextSystemDate
     * 系统下一日期
     * <NxtSysDt>  [1..1]  ISODate  禁止中文
     */
    private String nxtSysDt;

    /**
     * ServiceInformation  <SvcInf>  [1..n]
     */
    private List<BATNServiceInformationDto> svcInf;
}
