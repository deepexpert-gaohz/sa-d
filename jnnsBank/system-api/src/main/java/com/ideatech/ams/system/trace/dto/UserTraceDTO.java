package com.ideatech.ams.system.trace.dto;

import com.ideatech.ams.system.trace.enums.OperateModule;
import com.ideatech.ams.system.trace.enums.OperateType;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import java.util.Date;

/**
 * @author jzh
 * @date 2019-10-30.
 */

@Data
public class UserTraceDTO extends BaseMaintainableDto {

    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 操作人（机构+中文名）
     */
    private String operateName;

    /**
     * 机构fullId
     */
    private String organFullId;

    /**
     * 操作时间
     */
    private Date operateDate;

    /**
     * 调用类
     */
    private String className;

    /**
     * 调用方法
     */
    private String methodName;

    /**
     * 操作模块
     */
    private OperateModule operateModule;

    /**
     * 操作类型
     */
    private OperateType operateType;

    /**
     * 操作结果
     */
    private Boolean operateResult;

    /**
     * 操作内容
     */
    private String operateContent;

    /**
     * 操作详情(具体参数)备用
     */
    private String operateDetail;

    /**
     * 异常详情
     */
    private String exceptionDetail;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 是否覆盖，在生成操作内容时使用，不进行存储。
     */
    private Boolean cover;
}
