package com.ideatech.ams.utils;


import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.ideatech.ams.controller.PbcSyncController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 敏感信息脱敏处理
 * @author AAA
 */
public class SensitiveDataConverter extends ClassicConverter {
    public static Logger logger = LoggerFactory.getLogger(SensitiveDataConverter.class);

    @Override
    public String convert(ILoggingEvent event){
        // 获取原始日志
        String oriLogMsg = event.getFormattedMessage();


        // 获取脱敏后的日志
        String afterLogMsg = invokeMsg(oriLogMsg);
//        logger.info("获取脱敏后的日志================="+afterLogMsg);
        return afterLogMsg;

    }

    /**
     * 日志脱敏开关
     */
    private static String converterCanRun = "true";
    /**
     * 日志脱敏关键字
     */
    private static String sensitiveDataKeys = "legalIdcardNo,legalName,nontmpLegalName,parLegalName,insideLeadIdcardNo,telephone,parLegalIdcardNo,financeTelephone,insideTelephone,nontmpTelephone,regAddress,insideAddress,nontmpAddress,regFullAddress,bankCardNo,nontmpLegalIdcardNo";

    /**
     * 处理日志字符串，返回脱敏后的字符串
     * @param
     * @return
     */
    public String invokeMsg(final String oriMsg){
        String tempMsg = oriMsg;
        if("true".equals(converterCanRun)){
            // 处理字符串
            if(sensitiveDataKeys != null && sensitiveDataKeys.length() > 0){
                String[] keysArray = sensitiveDataKeys.split(",");
                for(String key: keysArray){
                    int index= -1;
                    do{
                        index = tempMsg.indexOf(key, index+1);
                        if(index != -1){
                            // 判断key是否为单词字符
                            if(isWordChar(tempMsg, key, index)){
                                continue;
                            }
                            // 寻找值的开始位置
                            int valueStart = getValueStartIndex(tempMsg, index + key.length());

                            // 查找值的结束位置（逗号，分号）........................
                            int valueEnd = getValuEndEIndex(tempMsg, valueStart);

                            // 对获取的值进行脱敏
                            String subStr = tempMsg.substring(valueStart, valueEnd);
                            subStr = tuomin(subStr, key);
                            ///////////////////////////
                            tempMsg = tempMsg.substring(0,valueStart) + subStr + tempMsg.substring(valueEnd);
                        }
                    }while(index != -1);
                }
            }
        }
        return tempMsg;
    }


    private static Pattern pattern = Pattern.compile("[0-9a-zA-Z]");
    /**
     * 判断从字符串msg获取的key值是否为单词 ， index为key在msg中的索引值
     * @return
     */
    private boolean isWordChar(String msg, String key, int index){

        // 必须确定key是一个单词............................
        if(index != 0){ // 判断key前面一个字符
            char preCh = msg.charAt(index-1);
            Matcher match = pattern.matcher(preCh + "");
            if(match.matches()){
                return true;
            }
        }
        // 判断key后面一个字符
        char nextCh = msg.charAt(index + key.length());
        Matcher match = pattern.matcher(nextCh + "");
        if(match.matches()){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String tempMsg = "{sign=f88898b2677e62f1ad54b9e330c0a27e, idcard=130333198901192762, realname=%E5%BE%90%E5%BD%A6%E5%A8%9C, key=c5d34d4c3c71cc45c88f32b4f13da887, mobile=13210141605, bankcard=6226430106137525}";
        String tempMsg1 = "{\"reason\":\"成功 \",\"result\":{\"jobid\":\"JH2131171027170837443588J6\",\"realname\":\"李哪娜\",\"bankcard\":\"6226430106137525\",\"idcard\":\"130333198901192762\",\"mobile\":\"13210141605\",\"res\":\"1\",\"message\":\"验证成功\"},\"error_code\":0}";
        SensitiveDataConverter sc = new SensitiveDataConverter();
        System.out.println(sc.invokeMsg(tempMsg));
        System.out.println(sc.invokeMsg(tempMsg1));
    }

    /**
     * 获取value值的开始位置
     * @param msg 要查找的字符串
     * @param valueStart 查找的开始位置
     * @return
     */
    private int getValueStartIndex(String msg, int valueStart ){
        // 寻找值的开始位置.................................
        do{
            char ch = msg.charAt(valueStart);
            if(ch == ':' || ch == '='){ // key与 value的分隔符
                valueStart ++;
                ch = msg.charAt(valueStart);
                if(ch == '"'){
                    valueStart ++;
                }
                break;    // 找到值的开始位置
            }else{
                valueStart ++;
            }
        }while(true);
        return valueStart;
    }

    /**
     * 获取value值的结束位置
     * @return
     */
    private int getValuEndEIndex(String msg,int valueEnd){
        do{
            if(valueEnd == msg.length()){
                break;
            }
            char ch = msg.charAt(valueEnd);

            if(ch == '"'){ // 引号时，判断下一个值是结束，分号还是逗号决定是否为值的结束
                if(valueEnd+1 == msg.length()){
                    break;
                }
                char nextCh = msg.charAt(valueEnd+1);
                if(nextCh ==';' || nextCh == ','){
                    // 去掉前面的 \  处理这种形式的数据
                    while(valueEnd>0 ){
                        char preCh = msg.charAt(valueEnd-1);
                        if(preCh != '\\'){
                            break;
                        }
                        valueEnd--;
                    }
                    break;
                }else{
                    valueEnd ++;
                }
            }else if (ch ==';' || ch == ',' || ch == '}'){
                break;
            }else{
                valueEnd ++;
            }

        }while(true);
        return valueEnd;
    }

    private String tuomin(String submsg, String key){
        // idcard：身份证号, realname：姓名, bankcard：银行卡号, mobile：手机号
        if("legalIdcardNo".equals(key)){
            return SensitiveInfoUtils.idCardNum(submsg);
        }
        if("insideLeadIdcardNo".equals(key)){
            return SensitiveInfoUtils.idCardNum(submsg);
        }
        if("parLegalIdcardNo".equals(key)){
            return SensitiveInfoUtils.idCardNum(submsg);
        }
        if("nontmpLegalIdcardNo".equals(key)){
            return SensitiveInfoUtils.idCardNum(submsg);
        }
        if("legalName".equals(key)){
            return SensitiveInfoUtils.chineseName(submsg);
        }
        if("nontmpLegalName".equals(key)){
            return SensitiveInfoUtils.chineseName(submsg);
        }
        if("parLegalName".equals(key)){
            return SensitiveInfoUtils.chineseName(submsg);
        }
        if("regAddress".equals(key)){
            return SensitiveInfoUtils.chineseName(submsg);
        }
        if("insideAddress".equals(key)){
            return SensitiveInfoUtils.chineseName(submsg);
        }
        if("nontmpAddress".equals(key)){
            return SensitiveInfoUtils.chineseName(submsg);
        }
        if("regFullAddress".equals(key)){
            return SensitiveInfoUtils.chineseName(submsg);
        }

        if("bankCardNo".equals(key)){
            return SensitiveInfoUtils.bankCard(submsg);
        }
        if("telephone".equals(key)){
            return SensitiveInfoUtils.mobilePhone(submsg);
        }
        if("financeTelephone".equals(key)){
            return SensitiveInfoUtils.mobilePhone(submsg);
        }
        if("insideTelephone".equals(key)){
            return SensitiveInfoUtils.mobilePhone(submsg);
        }
        if("nontmpTelephone".equals(key)){
            return SensitiveInfoUtils.mobilePhone(submsg);
        }

        return "";
    }
}