package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import com.ideatech.common.validator.ValidateDateString;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author wangqingan
 * @version 12/02/2018 10:50 AM
 */
@Data
public class SupplementDto extends BaseMaintainableDto {

//    @NotNull(message = "补录用户对应工商ID不能为空")
    private Long id;

    @NotEmpty(message="补录用户类型不能为空")
    private String type;

    /**
     * 对应Beneficiary的type字段，新增受益人时使用
     * Dto的tpye值被占用，所以用beneficiaryType字段代替，对应Beneficiary实体的type
     */
    private String beneficiaryType;

    @NotNull(message = "补录用户对应工商ID不能为空")
    private Long saicInfoId;

    @NotEmpty(message = "姓名不能为空")
    private String name;

    private String address;

    //@ValidateString(acceptedValues = {},message = "证件类型不存在")
//    @Pattern(regexp = "[0-9]{1,2}", message = "证件类型不存在")
    protected String idCardType;

//    @NotEmpty(message = "证件号码不能为空")
    private String idCardNo;

    @ValidateDateString(message = "证件到期日格式不对", required = false)
    private String idCardDue;

    @Pattern(regexp = "^0?(13|14|15|17|18|19)[0-9]{9}$|^$", message = "联系电话格式不对")
    private String telephone;

    private String sex;

    @ValidateDateString(message = "出生日期格式不对", required = false)
    private String dob;

    private String nationality;

    @ValidateDateString(message = "证件起始日格式不对", required = false)
    private String idCardStart;

    private String identifyno;

    private String capitalpercent;

    @ValidateDateString(message = "出资日期格式不对", required = false)
    private String condate;//出资日期
    private String fundedratio;//出资比例
    private String regcapcur;//币种
    private String realamount;//实缴出资额
    private String realtype;//实缴出资方式
    @ValidateDateString(message = "实缴出资日期格式不对", required = false)
    private String realdate;//实缴出资日期
    private String subconam;//认缴出资额
    private String investtype;//认缴出资方式
}
