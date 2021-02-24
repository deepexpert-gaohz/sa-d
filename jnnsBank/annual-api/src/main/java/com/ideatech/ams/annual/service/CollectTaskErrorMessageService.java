package com.ideatech.ams.annual.service;


import com.ideatech.ams.annual.dto.CollectErrorMessageSearchDto;
import com.ideatech.ams.annual.dto.CollectTaskErrorMessageDto;

import java.util.List;

/**
 * @Description 采集的错误信息
 * @Author wanghongjie
 * @Date 2018/10/8
 **/
public interface CollectTaskErrorMessageService {
    List<CollectTaskErrorMessageDto> findByTaskIdAndAnnualTaskId(Long taskId,Long annualTaskId);
    CollectErrorMessageSearchDto search(final CollectErrorMessageSearchDto collectErrorMessageSearchDto, final Long taskId, final Long annualTaskId);
}
