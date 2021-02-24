package com.ideatech.ams.mivs.dto.end;

import com.ideatech.ams.mivs.dto.MessageHeaderDto;
import lombok.Data;

/**
 * @author jzh
 * @date 2019/7/25.
 */
@Data
public class EnterpriseAbnormalNoticeDto extends MessageHeaderDto {

    /******AbnmlCompany*****/
    /**
     * 单位名称
     */
    private String coNm;

    /**
     * 统一社会信用代码
     */
    private String uniSocCdtCd;
    /******AbnmlCompany*****/

    /******AbnmlPhoneNumber*****/
    /**
     * 手机号码
     */
    private String phNb;

    /**
     * 姓名
     */
    private String nm;
    /******AbnmlPhoneNumber*****/


    /**
     * 异常核查类型
     */
    private String abnmlType;

    /**
     * 异常内容说明
     */
    private String dESC;
}
