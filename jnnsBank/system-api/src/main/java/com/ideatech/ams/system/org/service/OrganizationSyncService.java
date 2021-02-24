package com.ideatech.ams.system.org.service;

import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.dto.OrganizationSyncDto;
import com.ideatech.ams.system.org.dto.OrganizationSyncSearchDto;
import com.ideatech.ams.system.org.enums.SyncType;

import java.util.List;

public interface OrganizationSyncService {
    void save(OrganizationSyncDto organizationSyncDto);
    /**
     * 构造OrganizationSyncDto对象
     * @param organizationDto
     * @param syncType
     * @return
     */
    OrganizationSyncDto convertOrganizationSyncDto(OrganizationDto organizationDto, SyncType syncType,String originalPbcCode);

    /**
     * 根据创建时间升序获取同步状态列表
     * @param syncStatus
     * @return
     */
    List<OrganizationSyncDto> findBySyncFinishStatusOrderByCreatedDateAsc(Boolean syncStatus);

    OrganizationSyncSearchDto search(final OrganizationSyncSearchDto organizationSyncSearchDto);

    OrganizationSyncDto findById(Long id);
}
