package com.ideatech.ams.service.impl;

import com.ideatech.ams.dao.JnnsGetAcctNoDao;
import com.ideatech.ams.service.JnnsGetAcctNoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class JnnsGetAcctNoServiceImpl implements JnnsGetAcctNoService {

    @Autowired
    private JnnsGetAcctNoDao jnnsGetAcctNoDao;

    public List<String> findByAcctNo(String acctNo) {

            List<String> list = jnnsGetAcctNoDao.findCCodeByAcctNo(acctNo);
            log.info("list集合======="+list.toString());
            return list;

    }
}
