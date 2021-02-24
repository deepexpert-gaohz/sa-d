package com.ideatech.ams.customer.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author jzh
 * @date 2019/4/19.
 */

@Component
@Data
public class SaicStateDto {
    private Boolean state;
    private Long speed;
}
