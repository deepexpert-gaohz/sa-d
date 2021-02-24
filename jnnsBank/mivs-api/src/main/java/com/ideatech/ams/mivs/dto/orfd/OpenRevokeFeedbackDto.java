package com.ideatech.ams.mivs.dto.orfd;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author jzh
 * @date 2019/7/23.
 */

@Data
public class OpenRevokeFeedbackDto {

    private Long id;

    /**
     * --EntityName
     * 企业名称	<EntNm>	[0..1]	Max100Text	市场主体类型：企业
     */
    @Length(max = 100, message = "企业名称长度超过100")
    private String entNm;

    /**
     * --TradeName
     * 字号名称	<TraNm>	[0..1]	Max256Text	市场主体类型：个体户
     */
    @Length(max = 256, message = "字号名称长度超过256")
    private String traNm;

    /**
     * --UniformSocialCreditCode
     *    统一社会信用代码	<UniSocCdtCd>	[1..1]	Max18Text	企业客户的统一社会信用代码
     * 禁止中文
     */
    @NotBlank(message="统一社会信用代码不能为空")
    @Length(max = 18, message = "统一社会信用代码长度超过18")
    private String uniSocCdtCd;

    /**
     * --AccountStatus
     *    账户状态标识	<AcctSts>	[1..1]	CompanyAccountStatusCode (Max4Text)
     * 企业账户状态标识
     * OPEN：已开户
     * CLOS：已销户
     */
    @NotBlank(message="企业账户状态标识不能为空")
    @Length(max = 4, message = "企业账户状态标识长度超过4")
    private String acctSts;

    /**
     * --ChangeDate
     *    变更日期	<ChngDt>	[1..1]	ISODate
     */
    @NotBlank(message="变更日期不能为空")
    private String chngDt;
}
