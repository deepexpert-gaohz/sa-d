package com.ideatech.ams.account.service.bill;

import com.ideatech.ams.account.dao.bill.BillNoSeqDao;
import com.ideatech.ams.account.enums.bill.BillTypeNo;
import com.ideatech.common.entity.id.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author van
 * @date 21:24 2018/5/30
 */
@Service
@Transactional
@Slf4j
public class BillNoSeqServiceImpl implements BillNoSeqService {

    @Autowired
    private BillNoSeqDao billNoSeqDao;

    @Autowired
    private IdWorker idWorker;

    @Override
    public synchronized String getBillNo(String date, String orgCode, BillTypeNo type) {

        String billNo = date + orgCode + type.getValue() + String.format("%020d", idWorker.nextId());

        return billNo;
        /*
        Long seq = null;

        BillNoSeq billNoSeq = new BillNoSeq();

        if (StringUtils.isBlank(orgCode)) {
            orgCode = "RSVR";
        }
        billNoSeq = billNoSeqDao.findByTypeAndDateAndOrgCode(type, date, orgCode);

//        if (StringUtils.isNotBlank(orgCode)) {
//            billNoSeq = billNoSeqDao.findByTypeAndDateAndOrgCode(type, date, orgCode);
//        } else {
//            billNoSeq = billNoSeqDao.findByTypeAndDate(type, date);
//        }

        // 对象是否存在
        if (billNoSeq != null && billNoSeq.getId() > 0) {
            seq = billNoSeq.getSequence() + 1;
            billNoSeq.setSequence(seq);
            // 更新序列
            billNoSeqDao.save(billNoSeq);
        } else {
            BillNoSeq billNoSeq1 = new BillNoSeq();
            seq = 1L;
            billNoSeq1.setOrgCode(orgCode);
            billNoSeq1.setType(type);
            billNoSeq1.setDate(date);
            billNoSeq1.setSequence(seq);
            // 插入序列
            billNoSeqDao.save(billNoSeq1);
        }

        String billNo = date + orgCode + type.getValue() + String.format("%04d", seq);

        return billNo;*/
    }
}
