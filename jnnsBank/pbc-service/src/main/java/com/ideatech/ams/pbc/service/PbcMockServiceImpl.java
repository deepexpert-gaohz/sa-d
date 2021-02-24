package com.ideatech.ams.pbc.service;

import org.springframework.stereotype.Service;

/**
 * @author van
 * @date 18:03 2018/7/19
 */
@Service
public class PbcMockServiceImpl implements PbcMockService {


    @Override
    public Boolean isLoginMockOpen() {
        return false;
    }

    @Override
    public Boolean isSyncMockOpen() {
        return false;
    }
}
