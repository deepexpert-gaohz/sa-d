package com.ideatech.ams.system.org.vo;

import lombok.Data;

/**
 * @Description 同步机构--删除
 * @Author wanghongjie
 * @Date 2019/2/22
 **/
@Data
public class DeleteSyncOrg extends SyncOrgVo{

    /**
     * 对外的银行联行号
     */
    private String cnapsCode;

    /**
     * 对外的上级银行联行号
     */
    private String parentCnapsCode;
}
