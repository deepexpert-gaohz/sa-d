package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.OrganizationAndPbcDto;

public interface OrganizationAndPbcService {
    OrganizationAndPbcDto findByFullId(String fullId);
    OrganizationAndPbcDto findById(Long id);
}
