package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class CarrierOperatorDto extends BaseMaintainableDto {
	//姓名
	private Long id;
	//姓名
	private String name;
	//手机号
	private String mobile;
	//身份证号码
	private String cardno;
	//查询结果状态(success成功，fail失败)
	private String status;
	//查询失败原因
	private String reason;
	//查询结果
	private String result;

}
