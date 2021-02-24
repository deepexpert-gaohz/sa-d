package com.ideatech.ams.system.annotation.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.system.annotation.dao.MessageLogDao;
import com.ideatech.ams.system.annotation.dto.MessageLogDto;
import com.ideatech.ams.system.annotation.entity.MessageLog;
import com.ideatech.ams.system.annotation.enums.SendStatusEnum;
import com.ideatech.ams.system.annotation.service.MessageLogService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
@Slf4j
public class InterfaceLogAspect {

    @Autowired
    private MessageLogService messageLogService;

    @Pointcut("@annotation(com.ideatech.common.annotation.InterfaceLog)")
    public void interfaceAspect() {
    }

    @Around("interfaceAspect()")
    public Object doAfter(ProceedingJoinPoint proceedingJoinPoint) {
        MessageLogDto messageLogDto = new MessageLogDto();
        Object rspObject = null;

        String reqMessage = "";
        String resMessage = "";

        try {
            // 出参
            rspObject = proceedingJoinPoint.proceed();
            // 入参
            Object[] arguments = proceedingJoinPoint.getArgs();

            Signature signature = proceedingJoinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            //获取到方法的所有参数名称的字符串数组
            String[] parameterNames = methodSignature.getParameterNames();

            //把账号字段展示为基本户许可证
            for(int i=0;i<parameterNames.length;i++){
                if(parameterNames[i].equals("accountKey")){
                    messageLogDto.setAcctNo((String) arguments[i]);
                    break;
                }
            }

            for(Object reqObject : arguments){
                reqMessage += obj2MessageReqLog(reqObject, messageLogDto,reqMessage);
            }
            if(StringUtils.isNotBlank(reqMessage)) {
                messageLogDto.setRequestMsg(reqMessage);
            }
            resMessage += obj2MessageResLog(rspObject, messageLogDto,resMessage);
            if(StringUtils.isNotBlank(resMessage)) {
                messageLogDto.setResponseMsg(resMessage);
            }
            //请求类名，方法名：
            String class_name = proceedingJoinPoint.getTarget().getClass().getName();
            String method_name = proceedingJoinPoint.getSignature().getName();
            if(class_name.contains("Pbc")){
                messageLogDto.setReqMethod(class_name + "." + method_name);
                //根据请求的方法判断是查询还是报送
                if(method_name.contains("check") || method_name.contains("Check")){
                    messageLogDto.setTranType("AMSCheck");
                }
                if(method_name.contains("sync") || method_name.contains("Sync")){
                    messageLogDto.setTranType("AMSSync");
                }
            }
            messageLogDto.setTranDate(DateUtils.DateToStr(new Date(),"yyyy-MM-dd"));
            messageLogDto.setTranTime(DateUtils.DateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
            messageLogService.saveMessageLog(messageLogDto);
        } catch (Throwable e) {
            messageLogDto.setErrorCode("错误信息：" + e.getMessage());
            messageLogDto.setProcessResult("FAIL");
            messageLogDto.setTranDate(DateUtils.DateToStr(new Date(),"yyyy-MM-dd"));
            messageLogDto.setTranTime(DateUtils.DateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
            messageLogService.saveMessageLog(messageLogDto);
            return rspObject;
        }
        return rspObject;
    }

    private String obj2MessageReqLog(Object object, MessageLogDto messageLog,String reqMessage) {
        String className = object.getClass().getSimpleName();

        switch (className) {
            //返回结果
            case "String":
                reqMessage = "String:" + object + ";";
                break;
            case "AllBillsPublicDTO":
                JSONObject json = (JSONObject)JSON.toJSON(object);
                String acctNo = (String)json.get("acctNo");
                String billType = (String)json.get("billType");
                if(StringUtils.isNotBlank(acctNo)){
                    messageLog.setAcctNo(acctNo);
                }
                if(StringUtils.isNotBlank(billType)){
                    messageLog.setBillType(billType);
                }
                reqMessage = "AllBillsPublicDTO:" + json.toString();
                break;
            case "boolean":
                reqMessage = "boolean:" + object + ";";
                break;
            default:
                break;
        }
        return reqMessage;
    }

    private String obj2MessageResLog(Object object, MessageLogDto messageLog,String resMessage) {
        String className = object.getClass().getSimpleName();

        switch (className) {
            //返回结果
            case "ResultDto":
                ResultDto resultDto = (ResultDto)object;
                if("1".equals(resultDto.getCode())){
                    resMessage = "ResultDto：" + JSON.toJSONString(resultDto);
                    messageLog.setResponseMsg(resMessage);
                    messageLog.setProcessResult("SUCCESS");
                }else{
                    messageLog.setErrorCode(resultDto.getMessage());
                    messageLog.setProcessResult("FAIL");
                }
                break;
            default:
                break;
        }
        return resMessage;
    }
}
