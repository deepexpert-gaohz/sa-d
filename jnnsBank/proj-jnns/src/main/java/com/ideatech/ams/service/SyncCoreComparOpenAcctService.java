package com.ideatech.ams.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
@Service
public interface SyncCoreComparOpenAcctService {

    void syncCoreCompare();

    void getOrganCode(String kaixhubz);

    @Transactional
    void delCoreData();

    @Transactional
    void insertYuJingData(Pageable pageable );


}
