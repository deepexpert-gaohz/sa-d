package com.ideatech.ams.system.trace.service;

import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.trace.dto.UserTraceDTO;
import com.ideatech.ams.system.trace.enums.OperateType;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.*;

/**
 * 系统配置管理模块特定的用户操作痕迹处理类（用于切面）
 * @author jzh
 * @date 2019-11-01.
 */

@Slf4j
@Service
public class ConfigOperateLogServiceImpl extends AbstractOperateLogService{

    private static final Map<String,String> map = new HashMap<String,String>(){{
        put("resvrProcessDay","业务提醒管理");
        put("systemIP","定时任务执行IP");
        put("pbcEnabled","人行交互管理");
        put("organid","预约交互管理");
        put("annualAgainEnabled","年检配置");
        put("accountImageMockEnabled","影像服务配置");
        put("acctStatisticsRange","首页通知管理");
        put("import","初始导入管理");
        put("printf","打印管理");
        put("billLockConfigEnabled","业务流水管理");
        put("saicState","工商网络状态监测管理");
        put("titleName","LOGO管理");
        put("openSyncStatus","纯接口上报配置");
        put("stoppedCancelHezhun","停用取消核准业务上报");
        put("phoneMoney","第三方数据费用管理");
        put("heZhunAccountCheckSuspend","变更校验基本户久悬");
        put("stoppedVideoEnabled","双录配置");
        put("imageVideoRemind1","双录意愿提示配置");
        put("dualRecordRemind1","双录意愿提示配置");
        put("riskWarning1","双录意愿提示配置");
        put("pwdExpireCheck","用户登录管理");
    }};

    @Autowired
    private UserTraceService userTraceService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void process(JoinPoint joinPoint, UserTraceDTO userTraceDTO) throws Exception {
        super.process(joinPoint,userTraceDTO);
        setOperateContent(joinPoint,userTraceDTO);
        userTraceService.insert(userTraceDTO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void process(JoinPoint joinPoint, UserTraceDTO userTraceDTO, Object object) throws Exception {
        super.process(joinPoint,userTraceDTO);
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

        // 参数值
        Object[] args = joinPoint.getArgs();
        String tableName = null;
        List<ConfigDto> configDtoList = castList(args[0],ConfigDto.class);
        if (configDtoList != null && configDtoList.size()>0){
            for (ConfigDto configKey : configDtoList) {
                tableName = map.get(configDtoList.get(0).getConfigKey());
                if (tableName!=null){
                    userTraceDTO.setOperateContent(tableName);
                    break;
                }
            }
        }
        if (tableName == null){
            log.warn("系统配置管理table页面识别异常");
            throw new Exception("系统配置管理table页面识别异常");
        }

        String message = message = MessageFormat.format("在{0}中对{1}页面进行修改保存", userTraceDTO.getOperateModule().getName(), tableName);
        userTraceDTO.setOperateContent(message);
    }

}
