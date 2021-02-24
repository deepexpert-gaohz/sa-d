package com.ideatech.ams.system.config.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liangding
 * @create 2018-05-15 下午4:23
 **/
@Data
public class ConfigDto implements Serializable {
    private String configKey;
    private String configValue;
}
