package com.ideatech.ams.readData.service;

import com.ideatech.ams.readData.AlteritemMointor;
import com.ideatech.common.msg.TableResultResponse;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;

public interface JnnsAlteritemService {

    @Transactional
    TableResultResponse<AlteritemMointor> query(AlteritemMointor dto, Pageable pageable);
    @Transactional
    TableResultResponse<AlteritemMointor> query1(AlteritemMointor dto, Pageable pageable);
}
