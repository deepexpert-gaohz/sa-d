package com.ideatech.ams.system.whitelist.dto;

import com.ideatech.ams.system.whitelist.enums.WhiteListEntrySource;
import lombok.Data;

import java.util.List;

@Data
public class WhiteListDto {

    private Long id;
    private String entName;
    private WhiteListEntrySource source;
    private Long orgId;
    private String orgName;
    private String organCode;
    private String organFullId;
    /**
     * 状态
     */
    private String status;

    private List<String> statusList;
    /**
     * 扩展字段
     */
    private String string001;

    private String string002;

    private String string003;

    private String string004;

    private String string005;

    private String string007;

    private String string008;

    private String string009;

    private String string010;
}
