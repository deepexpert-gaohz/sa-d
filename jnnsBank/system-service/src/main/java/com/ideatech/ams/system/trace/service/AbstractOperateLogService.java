package com.ideatech.ams.system.trace.service;

import com.ideatech.ams.system.trace.dto.UserTraceDTO;
import com.ideatech.common.util.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jzh
 * @date 2019-11-01.
 */
public abstract class AbstractOperateLogService implements OperateLogService{


    /**
     * 共有属性设置
     * @param joinPoint
     * @param userTraceDTO
     * @throws Exception
     */
    @Override
    public void process(JoinPoint joinPoint, UserTraceDTO userTraceDTO) throws Exception{
        //默认成功
        userTraceDTO.setOperateResult(Boolean.TRUE);
        userTraceDTO.setUsername(SecurityUtils.getCurrentUsername());
        userTraceDTO.setOrganFullId(SecurityUtils.getCurrentOrgFullId());
        userTraceDTO.setOperateDate(new Date());
        userTraceDTO.setClassName(joinPoint.getSignature().getDeclaringTypeName());
        userTraceDTO.setMethodName(joinPoint.getSignature().getName());
    }

    /**
     * 设置共有属性（抛出异常时）
     * @param joinPoint
     * @param userTraceDTO
     * @param e
     * @throws Exception
     */
    @Override
    public void process(JoinPoint joinPoint, UserTraceDTO userTraceDTO, Throwable e)throws Exception {
        process(joinPoint,userTraceDTO);
        userTraceDTO.setOperateResult(Boolean.FALSE);
        userTraceDTO.setExceptionDetail(e.toString().length()<1000 ? e.toString() : e.toString().substring(0,1000));
    }

    /**
     * 从方法中或请求中获取参数值
     * 以对象形式接收的，在方法中无法获取参数值，需要从请求中获取。
     * @param joinPoint
     * @param parameter
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Object getParameter(JoinPoint joinPoint,String parameter) throws Exception{

        // 参数值
        Object[] args = joinPoint.getArgs();
        // 参数名
        String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();

        //参数打印
        for (int i = 0,length = argNames.length;i<length;i++) {
            if (parameter.equals(argNames[i])){
                return args[i];
            }
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getParameter(parameter);
    }

    /**
     * 参数装换
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    public  <T> List<T> castList(Object obj, Class<T> clazz)
    {
        List<T> result = new ArrayList<T>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }
}
