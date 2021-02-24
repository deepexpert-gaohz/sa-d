package com.ideatech.ams.system.trace.service;

import com.ideatech.ams.system.trace.dto.UserTraceDTO;
import com.ideatech.ams.system.trace.enums.OperateType;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.msg.ObjectRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认的用户操作痕迹处理类（用于切面）
 * @author jzh
 * @date 2019-10-30.
 */

@Slf4j
@Service
public class DefaultOperateLogServiceImpl extends AbstractOperateLogService{

    private List<OperateType> operateTypeList = new ArrayList<OperateType>(){{
        add(OperateType.UPDATE);
        add(OperateType.DELETE);
        add(OperateType.DISABLE);
        add(OperateType.ENABLE);
    }};

    @Autowired
    private UserTraceService userTraceService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void process(JoinPoint joinPoint, UserTraceDTO userTraceDTO)throws Exception {
        super.process(joinPoint,userTraceDTO);
        setOperateContent(joinPoint,userTraceDTO);
        userTraceService.insert(userTraceDTO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void process(JoinPoint joinPoint, UserTraceDTO userTraceDTO, Object object)throws Exception {
        super.process(joinPoint,userTraceDTO);
        setOperateResult(joinPoint,userTraceDTO,object);
        setOperateContent(joinPoint,userTraceDTO);
        userTraceService.insert(userTraceDTO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void process(JoinPoint joinPoint, UserTraceDTO userTraceDTO, Throwable e)throws Exception {
        super.process(joinPoint,userTraceDTO,e);
        setOperateContent(joinPoint,userTraceDTO);
        userTraceService.insert(userTraceDTO);
    }



    /**
     * 设置操作结果
     * @param joinPoint
     * @param userTraceDTO
     * @param object
     */
    private void setOperateResult(JoinPoint joinPoint, UserTraceDTO userTraceDTO, Object object){
        try{
            ResultDto resultDto = (ResultDto)object;
            if (resultDto!=null && ResultCode.NACK.equals(resultDto.getCode())){
                userTraceDTO.setOperateResult(Boolean.FALSE);
            }
        }catch (ClassCastException e){
            log.warn("保存用户操作痕迹时:ResultDto判断结果异常，开始强制装换成ObjectRestResponse。");
            try{
                ObjectRestResponse objectRestResponse = (ObjectRestResponse)object;
                if (objectRestResponse!=null && !objectRestResponse.isRel()){
                    userTraceDTO.setOperateResult(Boolean.FALSE);
                }
            }catch (ClassCastException e2){
                log.warn("保存用户操作痕迹时:ObjectRestResponse判断结果异常。");
            }
            //TODO 尝试强转其他形式的返回结果
        }
    }

    /**
     * 设置用户操作记录内容
     * @param joinPoint
     * @param userTraceDTO
     */
    private void setOperateContent(JoinPoint joinPoint, UserTraceDTO userTraceDTO)throws Exception{
        OperateType operateType = userTraceDTO.getOperateType();
        String message = null;

        //1、OperateContent为空时
        if (StringUtils.isBlank(userTraceDTO.getOperateContent())){
            if (operateTypeList.contains(operateType)){
                Object id = getParameter(joinPoint,"id");
                message = MessageFormat.format("在{0}中对ID:{1}进行{2}", userTraceDTO.getOperateModule().getName(), String.valueOf(id), operateType.getName());
            }else {
                message = MessageFormat.format("在{0}中进行{1}", userTraceDTO.getOperateModule().getName(), operateType.getName());
            }
            userTraceDTO.setOperateContent(message);
        //2、OperateContent不为空且覆盖时
        }else if (userTraceDTO.getCover()){
            Object id = getParameter(joinPoint,"id");
            message = MessageFormat.format("在{0}中对ID:{1}进行{2}", userTraceDTO.getOperateModule().getName(), String.valueOf(id), userTraceDTO.getOperateContent());
            userTraceDTO.setOperateContent(message);

        //3、OperateContent不为空且不覆盖时
        }else {
            //保持不变
        }
    }

}
