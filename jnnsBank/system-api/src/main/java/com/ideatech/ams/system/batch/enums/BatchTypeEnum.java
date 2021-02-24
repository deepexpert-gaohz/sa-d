package com.ideatech.ams.system.batch.enums;

import lombok.Getter;

/**
 * 批次类型
 *
 * @author vantoo
 */
@Getter
public enum BatchTypeEnum {

    TPO("T+1"),
    BATCH_SUSPEND("批量久悬"),
    ILLEGAL_INQUIRY("违法查询"),
    BENEFICIARY("受益人比对"),
    STOCKHOLDER("控股股东比对"),
    TELECOM_3EL("电信三要素校验"),
    BATCH_CUSTOMERTUNE("批量客户尽调"),
    BENEFICIARYNAME("受益人名称比对"),
    BENEFICIARYCOLLECT("受益人采集");

    private String value;

    BatchTypeEnum(String value) {
        this.value = value;
    }

}
