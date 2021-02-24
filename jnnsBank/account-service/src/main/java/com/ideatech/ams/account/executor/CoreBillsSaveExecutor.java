package com.ideatech.ams.account.executor;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.bill.BillTypeNo;
import com.ideatech.ams.account.service.bill.BillNoSeqService;
import com.ideatech.ams.account.service.core.AmsCoreAccountService;
import com.ideatech.ams.account.service.core.AmsCoreAccountServiceImpl;
import com.ideatech.ams.pbc.utils.DateUtils;
import com.ideatech.ams.system.user.dto.UserDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @Description 核心数据导入时多线程进行数据保存
 * @Author wanghongjie
 * @Date 2018/9/16
 **/
@Data
@Slf4j
public class CoreBillsSaveExecutor  implements Callable {

    private AmsCoreAccountService amsCoreAccountService;

    private PlatformTransactionManager transactionManager;

    private BillNoSeqService billNoSeqService;

    private UserDto userDto;

    @Override
    public Object call() throws Exception {
        TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transaction = transactionManager.getTransaction(definition);
        int index=0;
        log.info("开始进行核心数据的保存");
        while(AmsCoreAccountServiceImpl.coreObjectSaveFlag){
            Iterator<Map.Entry<String, AllBillsPublicDTO>> iterator = AmsCoreAccountServiceImpl.coreObjectMaps.entrySet().iterator();
            while(iterator.hasNext()){
                if(index >0 && index%20 ==0){
                    try{
                        log.info("已经保存核心数据处理" + index + "条账户");
                        transactionManager.commit(transaction);
                    }catch (Exception e){
                        log.error("批量核心数据保存失败" ,e);
                    }finally {
                        transaction = transactionManager.getTransaction(definition);
                    }
                }
                try{
                    Map.Entry<String, AllBillsPublicDTO> next = iterator.next();
                    AllBillsPublicDTO allBillsPublicDto = next.getValue();
                    if (StringUtils.isBlank(allBillsPublicDto.getBillNo())) {
                        String billNo = billNoSeqService.getBillNo(DateUtils.DateToStr(new Date(), "yyyyMMdd"), allBillsPublicDto.getOrganCode(), BillTypeNo.valueOf(allBillsPublicDto.getBillType().name()));
                        // 单据编号
                        allBillsPublicDto.setBillNo(billNo);
                    }
                    amsCoreAccountService.checkIsNull(allBillsPublicDto,userDto);
                    iterator.remove();
                    log.info("完成第{}条数据保存，账户号为{}",index,allBillsPublicDto.getAcctNo());
                }catch (Exception e){
                    log.error("处理核心存量数据提交时异常", e);
                    transactionManager.rollback(transaction);
                    transaction = transactionManager.getTransaction(definition);
                }finally {
                    index++;
                }
            }
        }
        transactionManager.commit(transaction);
        log.info("核心数据的保存完成");
        return System.currentTimeMillis();
    }
}
