package com.ideatech.ams.system.trace.service;

import com.ideatech.ams.system.trace.dto.UserTraceDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * 证件到期提醒导出
 * @author jzh
 * @date 2019-11-01.
 */

@Slf4j
@Service
public class NoticeExportOperateLogServiceImpl extends AbstractOperateLogService{

    @Autowired
    private UserTraceService userTraceService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void process(JoinPoint joinPoint, UserTraceDTO userTraceDTO) throws Exception {
        super.process(joinPoint, userTraceDTO);
        setOperateContent(joinPoint,userTraceDTO);
        userTraceService.insert(userTraceDTO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void process(JoinPoint joinPoint, UserTraceDTO userTraceDTO, Object object) throws Exception {
        super.process(joinPoint, userTraceDTO);
        setOperateContent(joinPoint,userTraceDTO);
        userTraceService.insert(userTraceDTO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void process(JoinPoint joinPoint, UserTraceDTO userTraceDTO, Throwable e) throws Exception {
        super.process(joinPoint,userTraceDTO,e);
        setOperateContent(joinPoint,userTraceDTO);
        userTraceService.insert(userTraceDTO);
    }

    /**
     * 设置用户操作记录内容
     * @param joinPoint
     * @param userTraceDTO
     */
    private void setOperateContent(JoinPoint joinPoint, UserTraceDTO userTraceDTO)throws Exception{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String noticeType = request.getParameter("noticeType");
        if ("legalDueNotice".equals(noticeType)){
            userTraceDTO.setOperateContent("在法人证件到期提醒页面进行导出");
        }else if ("fileDueNotice".equals(noticeType)){
            userTraceDTO.setOperateContent("在证明文件到期提醒页面进行导出");
        }else if ("operatorDueNotice".equals(noticeType)){
            userTraceDTO.setOperateContent("在经办人证件到期提醒页面进行导出");
        }else {
            log.warn("证件到期提醒noticeType识别异常");
            throw new Exception("证件到期提醒noticeType识别异常");
        }
    }
}
