package com.ideatech.ams.system.blacklist.dto;

import com.ideatech.ams.system.blacklist.enums.EBlackListEntrySource;
import com.ideatech.common.dto.PagingDto;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liangding
 * @create 2018-06-26 下午1:44
 **/
@Data
public class BlackListSearchDto extends PagingDto<BlackListEntryDto> implements Serializable {
    private String entName;
    private EBlackListEntrySource source;
    private String level;
    private String type;
}
