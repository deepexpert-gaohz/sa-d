package com.ideatech.ams.apply.enums;

import java.util.HashMap;
import java.util.Map;

public enum ApplyEnum implements BaseEnum<ApplyEnum, String>{
  UnComplete("UnComplete","待受理"),
  SUCCESS("SUCCESS","受理成功"),
  FAIL("FAIL","受理退回"),
  BuLuZhong("BuLuZhong","补录中"),

  APPROVING("APPROVING", "审核中"),
  APPROVED("APPROVED", "审核通过"),
  Complete("Complete","开户中"),
  REGISTER_SUCCESS("REGISTER_SUCCESS","开户成功"),
  REGISTER_FAIL("REGISTER_FAIL","开户失败"),
  TRANSACTION_SUCCESS("TRANSACTION_SUCCESS","结算成功"),
  BREAK_APPOINT("BREAK_APPOINT","爽约");

  private String value;
  private String displayName;

  static Map<String,ApplyEnum> enumMap=new HashMap<String, ApplyEnum>();
  static{
    for(ApplyEnum type:ApplyEnum.values()){
      enumMap.put(type.getValue(), type);
    }
  }

  private ApplyEnum(String value,String displayName) {
    this.value=value;
    this.displayName=displayName;
  }

  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public static ApplyEnum getEnum(String value) {
    return enumMap.get(value);
  }
}
