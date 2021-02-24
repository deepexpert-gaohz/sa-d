/**
 * 
 */
package com.ideatech.ams.system.log.service;

import com.ideatech.ams.system.log.dto.SysLogInfo;

import java.util.List;


/**
 * @author zhailiang
 *
 */
public interface AdminLogService {

	/*DhtmlxGrid query(AdminLogInfo adminLogInfo, Pageable pageable);

	DhtmlxGrid queryScheduleLog();

	DhtmlxGrid queryScheduleLogDetails(String reportDate);*/

	List<SysLogInfo> querySystemLog();

}
