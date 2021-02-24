package com.ideatech.ams.compare.vo;

import com.ideatech.ams.compare.enums.CollectState;
import lombok.Data;

import java.util.Objects;

/**
 * @Description 比对管理--采集记录VO
 * @Author wanghongjie
 * @Date 2019/2/11
 **/
@Data
public class CompareCollectRecordVo {

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 客户名称
     */
    private String depositorName;

    /**
     * 注册号
     */
    private String regNo;

    /**
     * 行内机构号
     */
    private String organCode;
    /**
     * 机构fullid
     */
    private String organFullId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompareCollectRecordVo that = (CompareCollectRecordVo) o;
        return Objects.equals(acctNo, that.acctNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(acctNo);
    }
}
