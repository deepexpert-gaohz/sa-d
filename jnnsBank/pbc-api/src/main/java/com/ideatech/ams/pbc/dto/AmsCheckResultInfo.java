package com.ideatech.ams.pbc.dto;

import lombok.Data;

/**
 * 基于基本户开户许可证校验是否满足开户条件
 *
 * @auther zoulang
 * @create 2018-05-20 上午11:09
 **/
@Data
public class AmsCheckResultInfo {

    /**
     * 校验是否满足
     * <pre>
     * true:满足开户条件
     * false:不满足开户条件
     * </pre>
     */
    private boolean checkPass;

    /**
     * 不满足开户条件原因
     */
    private String notPassMessage;

    /**
     * 满足开户条件，返回查询的人行信息
     */
    private AmsAccountInfo amsAccountInfo;

}
