package com.ideatech.ams.system.whitelist.dto;

import com.ideatech.ams.system.whitelist.enums.WhiteListEntrySource;
import com.ideatech.common.dto.PagingDto;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liangding
 * @create 2018-06-26 下午1:44
 **/
@Data
public class WhiteListSearchDto extends PagingDto<WhiteListDto> implements Serializable {
    private String entName;
    private WhiteListEntrySource source;
    private String orgName;
}
