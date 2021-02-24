package com.ideatech.ams.apply.util;


import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntDto;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.ReflectionUtil;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author chenxf
 * @company ydrx
 * @date 2/8/2018
 * @description 短信格式化
 */

public class MsgFormat {

  private static String SUCCESS_MSG =
      "【账户管理平台】尊敬的客户，恭喜您！您的受理号为%s的开户预约已受理成功！请在%s%s前往%s网点进行开户，您可登录【账户管理平台】小程序查看回执详情。";

  private static String FAIL_MSG = "【账户管理平台】尊敬的客户，很遗憾！您的受理号为%s的开户预约已受理驳回！您可登录【账户管理平台】小程序查看回执详情。";

  private static String CODE_MSG = "【%s】您的手机绑定验证码为:%s,如非本人操作,请忽略。";


  public static String formatSuccess(String applyId, String dateStr, String timeStr, String bankNet) {
    try {
      if (StringUtils.isBlank(timeStr)) {
        timeStr = "";
      }
      Date bankDate = DateUtils.parse(dateStr, DateUtils.PARSE_PATTERNS[0]);
      return String.format(SUCCESS_MSG, applyId,
          DateUtils.DateToStr(bankDate, DateUtils.PARSE_PATTERNS[2]), timeStr, bankNet);

    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;

  }

  public static String formatFail(String applyId) {
    return String.format(FAIL_MSG, applyId);

  }

  public static String formatCode(String application, String veryCode) {
    return String.format(CODE_MSG, application, veryCode);

  }


  public static String formatMessage(String message, CompanyPreOpenAccountEntDto temp){
    Map<String, String> fieldMap = ReflectionUtil.getFieldMapJustStringAndDate(temp);
    String newMessage = message;
    Iterator<Map.Entry<String, String>> iterator = fieldMap.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<String, String> entry = iterator.next();
      newMessage = StringUtils.replace(newMessage,"{"+entry.getKey()+"}",entry.getValue());
    }
    return newMessage;
  }

}
