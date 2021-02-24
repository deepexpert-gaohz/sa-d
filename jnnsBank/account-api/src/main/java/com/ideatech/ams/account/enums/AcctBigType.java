package com.ideatech.ams.account.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: AcctBigType
 * @Description: 对公报备账户性质大类枚举
 * @author jogy.he
 * @date
 */
public enum AcctBigType {

  jiben("基本存款账户"),

  yiban("一般存款账户"),

  zhuanyong("专用存款账户"),

  linshi("临时存款账户"),

  yanzi("验资户临时存款账户"),

  zengzi("增资户临时存款账户"),
  /**
   * 从核心未识别的账户性质
   */
  unknow("未知账户性质 ");

  private String value;

  AcctBigType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public static AcctBigType str2enum(String acctType) {
    if (StringUtils.isBlank(acctType)) {
      return null;
    }
    if (acctType.equals("jiben") || acctType.equals("基本存款账户")) {
      return AcctBigType.jiben;
    } else if (acctType.equals("yiban") || acctType.equals("一般存款账户")) {
      return AcctBigType.yiban;
    } else if (acctType.equals("linshi") || acctType.equals("临时机构临时存款账户")) {
      return AcctBigType.linshi;
    } else if (acctType.equals("unknow") || acctType.equals("未知账户")) {
      return AcctBigType.unknow;
    }else if(acctType.equals("zhuanyong") || acctType.equals("专用存款账户")){
      return AcctBigType.zhuanyong;
    }
    return null;
  }

  public static AcctBigType getValue(String name) {
    AcctBigType[] values = AcctBigType.values();
    for (AcctBigType value : values) {
      if (StringUtils.equals(value.getValue(), name)) {
        return value;
      }
    }
    return null;
  }
}
