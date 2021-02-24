package com.ideatech.ams.annual.enums;

import com.ideatech.common.enums.BillType;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 *单边类型
 *
 * @author van
 * @date 10:21 2018/8/8
 */
@Getter
public enum ResultStatusEnum {
	PASS("年检通过"), FAIL("年检失败"), INIT("未年检"),NO_CHECK_ANNUAL("无需年检");
	private String name;

	ResultStatusEnum(String name) {
		this.name = name;
	}

	public static String getValueBy(String resultStatus) {
		if (StringUtils.isBlank(resultStatus)) {
			return null;
		}

		if (resultStatus.equals("PASS") || resultStatus.equals("年检通过")) {
			return ResultStatusEnum.PASS.name;
		} else if (resultStatus.equals("FAIL") || resultStatus.equals("年检失败")) {
			return ResultStatusEnum.FAIL.name;
		} else if (resultStatus.equals("INIT") || resultStatus.equals("未年检")) {
			return ResultStatusEnum.INIT.name;
		} else if (resultStatus.equals("NO_CHECK_ANNUAL") || resultStatus.equals("无需年检")) {
			return ResultStatusEnum.NO_CHECK_ANNUAL.name;
		}

		return null;
	}
}
