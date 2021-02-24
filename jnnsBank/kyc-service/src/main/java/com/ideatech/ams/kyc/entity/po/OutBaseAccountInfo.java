package com.ideatech.ams.kyc.entity.po;

import lombok.Data;

import java.util.List;

import com.ideatech.ams.kyc.entity.BaseAccount;

/**
 * @author wangqingan
 * @version 09/02/2018 6:00 PM
 */
@Data
public class OutBaseAccountInfo {
    private int total;

    List<BaseAccount> items;
}
