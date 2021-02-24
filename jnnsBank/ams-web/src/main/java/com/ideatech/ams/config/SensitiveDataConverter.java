package com.ideatech.ams.config;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.ideatech.common.util.SensitiveInfoUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 该日志脱敏类只能实现日志中含有legalIdcardNo=130333198901192762或legalIdcardNo:130333198901192762类似格式的字符串，
 * 才能进行脱敏处理成legalIdcardNo=130333********62或legalIdcardNo:130333********62。
 * 如果直接是输出130333198901192762这样身份证件号，无法实现脱敏处理。
 *
 * @author jzh
 * @date 2019-09-26.
 */

public class SensitiveDataConverter extends MessageConverter {

    private static Pattern pattern = Pattern.compile("[0-9a-zA-Z]");

    /**
     * 日志脱敏开关
     */
    private static final boolean CONVERTER_CAN_RUN = true;

    /**
     * 日志脱敏关键字
     */
    private static final String[] SENSITIVE_DATA_KEYS = {"legalperson", "legalName", "coreLegalName", "parLegalName", "name", "legalPerson",
            "telephone", "legalMobile", "legalTelephone", "insideTelephone", "financeTelephone", "nontmpTelephone", "parLegalTelephone",
            "legalIdcardNo", "cardNo", "fundManagerIdcardNo", "nontmpLegalIdcardNo", "financeIdcardNo", "idNo", "insideSaccdepmanNo", "moneyManagerCno", "eccsFaFangAppcreCode"};

    /**
     * 姓名 关键字列表
     */
    private static final String[] NAMES_KEYS = {"legalPerson", "legalName", "coreLegalName", "parLegalName", "name", "legalPerson"};
    /**
     * 手机号 关键字列表
     */
    private static final String[] TELEPHONE_KEYS = {"telephone", "legalMobile", "legalTelephone", "insideTelephone", "financeTelephone", "nontmpTelephone", "parLegalTelephone"};
    /**
     * 身份证号 关键字列表
     */
    private static final String[] ID_CARD_NO_KEYS = {"legalIdcardNo", "cardNo", "fundManagerIdcardNo", "nontmpLegalIdcardNo", "financeIdcardNo", "idNo", "insideSaccdepmanNo", "moneyManagerCno", "eccsFaFangAppcreCode"};

    /**
     * ams包名前缀
     */
    private static final String AMS_PACKAGE = "com.ideatech";

    /**
     * 忽略的关键字
     */
    private static final String[] PACKAGE_IGNORE_KEYS = {"ApplicationContextUtil"};

    @Override
    public String convert(ILoggingEvent event) {
        if (event.getLoggerName().startsWith(AMS_PACKAGE) && !ignoreKeys(event.getLoggerName())) {
            //html不过滤
            if (!event.getFormattedMessage().contains("<html>")) {
                return invokeMsg(event.getFormattedMessage());
            }
        }
        return event.getFormattedMessage();
    }

    /**
     * 是否包含忽略关键字
     * @param packageName
     * @return
     */
    private boolean ignoreKeys(String packageName) {
        for (String ignoreKey : PACKAGE_IGNORE_KEYS) {
            if (packageName.contains(ignoreKey)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 处理日志字符串，返回脱敏后的字符串
     *
     * @param oriMsg
     * @return
     */
    public String invokeMsg(final String oriMsg) {
        String tempMsg = oriMsg;
        try {
            int index;
            if (CONVERTER_CAN_RUN) {
                // 处理字符串
                for (String key : SENSITIVE_DATA_KEYS) {
                    index = -1;
                    do {
                        index = tempMsg.indexOf(key, index + 1);
                        if (index != -1) {

                            // 判断从字符串msg获取的key值是否为单词，index为key在msg中的索引值
                            if (isWordChar(tempMsg, key, index)) {
                                continue;
                            }
                            // 寻找值的开始位置
                            int valueStart = getValueStartIndex(tempMsg, index + key.length());

                            // 查找值的结束位置（逗号，分号）........................
                            int valueEnd = getValuEndEIndex(tempMsg, valueStart);

                            // 对获取的值进行脱敏
                            String subStr = new String(tempMsg.substring(valueStart, valueEnd));
                            subStr = desensitize(subStr, key);

                            tempMsg = new String(tempMsg.substring(0, valueStart)) + subStr + new String(tempMsg.substring(valueEnd));
                        }
                    } while (index != -1);
                }
            }
        } catch (Exception e) {
            //ignore
        }
        return tempMsg;
    }


    /**
     * 判断key是否为完整字符
     * 例如：key ="name",
     * 若msg="{"name":"jzh","age":18}",则返回true
     * 若msg="{"named":"jzh","age":18}",则返回false
     *
     * @return
     */
    private boolean isWordChar(String msg, String key, int index) {

        // 必须确定key是一个单词............................
        if (index != 0) { // 判断key前面一个字符
            char preCh = msg.charAt(index - 1);
            Matcher match = pattern.matcher(preCh + "");
            if (match.matches()) {
                return true;
            }
        }
        // 判断key后面一个字符
        char nextCh = msg.charAt(index + key.length());
        Matcher match = pattern.matcher(nextCh + "");
        if (match.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 获取value值的开始位置
     *
     * @param msg        要查找的字符串
     * @param valueStart 查找的开始位置
     * @return
     */
    private int getValueStartIndex(String msg, int valueStart) {
        // 寻找值的开始位置.................................
        do {
            char ch = msg.charAt(valueStart);
            // key与 value的分隔符
            if (ch == ':' || ch == '=') {
                valueStart++;
                ch = msg.charAt(valueStart);
                if (ch == '"') {
                    valueStart++;
                }
                ch = msg.charAt(valueStart);
                if (ch == ' ') {
                    valueStart++;
                }
                // 找到值的开始位置
                break;
            } else {
                valueStart++;
            }
        } while (true);
        return valueStart;
    }

    /**
     * 获取value值的结束位置
     *
     * @return
     */
    private int getValuEndEIndex(String msg, int valueEnd) {
        do {
            if (valueEnd == msg.length()) {
                break;
            }
            char ch = msg.charAt(valueEnd);

            // 引号时，判断下一个值是结束，分号还是逗号决定是否为值的结束
            if (ch == '"') {
                if (valueEnd + 1 == msg.length()) {
                    break;
                }
                char nextCh = msg.charAt(valueEnd + 1);
                if (nextCh == ';' || nextCh == ',' || nextCh == '>' || nextCh == ' ') {
                    // 去掉前面的 \  处理这种形式的数据
                    while (valueEnd > 0) {
                        char preCh = msg.charAt(valueEnd - 1);
                        if (preCh != '\\') {
                            break;
                        }
                        valueEnd--;
                    }
                    break;
                } else {
                    valueEnd++;
                }
            } else if (ch == ';' || ch == ',' || ch == '}' || ch == ' ') {
                break;
            } else {
                valueEnd++;
            }

        } while (true);
        return valueEnd;
    }

    /**
     * 不同类型的数据采用不同的脱敏处理方式。
     *
     * @param msg
     * @param key
     * @return
     */
    private String desensitize(String msg, String key) {
        if (Arrays.asList(NAMES_KEYS).contains(key)) {
            return SensitiveInfoUtils.chineseName(msg);
        } else if (Arrays.asList(TELEPHONE_KEYS).contains(key)) {
            return SensitiveInfoUtils.mobilePhone(msg);
        } else if (Arrays.asList(ID_CARD_NO_KEYS).contains(key)) {
            return SensitiveInfoUtils.idNum(msg);
        }
        return "";
    }
}
