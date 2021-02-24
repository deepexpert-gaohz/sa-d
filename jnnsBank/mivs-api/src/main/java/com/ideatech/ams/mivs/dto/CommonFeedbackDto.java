package com.ideatech.ams.mivs.dto;


import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author jzh
 * @date 2019-08-06.
 */

@Data
public class CommonFeedbackDto {

    private Long id;

    /**
     * 相关报文日志记录id
     */
    @NotNull(message="报文日志记录id不能为空")
    private Long msgLogId;

    /**
     * 报文类型
     */
    @NotBlank(message="报文类型不能为空")
    private String msgType;

    /**
     * Content
     * 疑义反馈内容
     * <Cntt>  [1..1]  Max256Text  允许中文
     */
    @NotBlank(message="疑义反馈内容不能为空")
    @Length(max = 256, message = "疑义反馈内容长度超过256")
    private String cntt;

    /**
     * ContactName
     * 联系人姓名
     * <ContactNm>  [1..1]  Max140Text
     */
    @NotBlank(message="联系人姓名不能为空")
    @Length(max = 140, message = "联系人姓名长度超过140")
    private String contactNm;

    /**
     * ContactNumber
     * 联系人电话
     * <ContactNb>  [1..1]  Max30Text  禁止中文
     */
    @NotBlank(message="联系人电话不能为空")
    @Length(max = 30, message = "联系人电话长度超过30")
    private String contactNb;

}
