package com.ideatech.ams.apply.service;

import com.ideatech.ams.apply.cryptography.CryptoInput;
import com.ideatech.ams.apply.cryptography.CryptoOutput;
import com.ideatech.ams.apply.cryptography.CryptoResultDto;
import com.ideatech.ams.system.org.dto.OrganizationSyncDto;
import com.ideatech.common.msg.ObjectRestResponse;

/**
 * 机构同步
 */
public interface SynchronizeOrgService {
    /**
     * 机构同步
     * @param organizationSyncDto
     */
    String syncOrg(OrganizationSyncDto organizationSyncDto);

    /**
     * 发送机构同步信息
     * @param cryptoInput
     * @param cryptoOutput
     * @param url
     * @return
     */
    String send(CryptoInput cryptoInput, CryptoOutput cryptoOutput, String url, CryptoResultDto cryptoResultDto);

}
