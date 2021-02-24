package com.ideatech.ams.system.blacklist.dto;

import com.ideatech.ams.system.blacklist.enums.EBlackListEntrySource;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liangding
 * @create 2018-06-26 上午10:56
 **/
@Data
public class BlackListEntryDto implements Serializable {
    private Long id;
    private String entName;
    private EBlackListEntrySource source;
    private String level;
    private String type;
    private Boolean isWhite;
}
