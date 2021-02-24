package com.ideatech.ams.account.executor;

import com.ideatech.ams.account.dao.core.CorePublicAccountDao;
import com.ideatech.ams.account.dao.core.CorePublicAccountFinishDao;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.entity.CorePublicAccount;
import com.ideatech.ams.account.entity.CorePublicAccountFinish;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillTypeNo;
import com.ideatech.ams.account.service.bill.BillNoSeqService;
import com.ideatech.ams.account.service.core.AmsCoreAccountService;
import com.ideatech.ams.account.service.core.AmsCoreAccountServiceImpl;
import com.ideatech.ams.pbc.utils.DateUtils;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.jpa.IdeaNamingStrategy;
import com.ideatech.common.util.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @Description 核心数据导入时多线程进行字典和数据的转化
 * @Author wanghongjie
 * @Date 2018/9/16
 **/
@Data
@Slf4j
public class CoreBillsInitExecutor implements Callable {

    public CoreBillsInitExecutor(Set<Object> objSet) {
        this.objSet = objSet;
    }
    private Set<Object> objSet;

    private String batch;

    private UserDto userDto;

    private BillNoSeqService billNoSeqService;

    private AmsCoreAccountService amsCoreAccountService;

    private PlatformTransactionManager transactionManager;

    private CorePublicAccountDao corePublicAccountDao;

    private CorePublicAccountFinishDao corePublicAccountFinishDao;

    @Override
    public Object call() throws Exception {

		TransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus transaction = null;
//        boolean commitFlag=false;
        Long coreId = null;
        if (objSet != null && objSet.size() > 0) {
            try {
                AllBillsPublicDTO allBillsPublicDTO = null;
                log.info("{}-核心数据转化开始,需要跑的数据量为{}",batch,objSet.size());
                Long startTime = System.currentTimeMillis();
                int index = 0;
                for(Object obj:objSet){
                    try{
                        coreId= null;
                        Map<String, String> hashMap = (Map<String, String>) obj;
                        Map<String, String> map = new LinkedCaseInsensitiveMap<String>();
                        map.putAll(hashMap);
                        if(map.containsKey(IdeaNamingStrategy.PREFIX+"id")) {
                            Object str = map.get(IdeaNamingStrategy.PREFIX + "id");
                            if(str != null){
                                if(str.getClass() == BigDecimal.class){
                                    coreId = ((BigDecimal)str).longValue();
                                }else if(str.getClass() == BigInteger.class){
                                    coreId = ((BigInteger)str).longValue();
                                }else if(str.getClass() == String.class){
                                    coreId = Long.valueOf((String)str);
                                }else if(str.getClass() == Long.class){
                                    coreId = (Long)str;
                                }
                            }
                        }
                        log.info("存量导入map转换对象allBillsPublicDTO开始...");
                        allBillsPublicDTO = amsCoreAccountService.convertDictionary(map);
                        log.info("存量导入map转换对象allBillsPublicDTO结束...");
                        //设置基本户注册地区代码
                        log.info("存量导入设置基本户注册地区代码...");
                        if(allBillsPublicDTO.getAcctType() == CompanyAcctType.yiban || allBillsPublicDTO.getAcctType() == CompanyAcctType.yusuan || allBillsPublicDTO.getAcctType() == CompanyAcctType.feiyusuan ||
                                allBillsPublicDTO.getAcctType() == CompanyAcctType.feilinshi){
                            if(StringUtils.isBlank(allBillsPublicDTO.getBasicAcctRegArea())){
                                allBillsPublicDTO.setBasicAcctRegArea(allBillsPublicDTO.getRegAreaCode());
                            }
                        }

                        log.info("存量数据初始化allBillsPublicDTO...");
                        amsCoreAccountService.initAllBillsPublicData(allBillsPublicDTO);
                        AmsCoreAccountServiceImpl.coreObjectMaps.put(allBillsPublicDTO.getAcctNo(),allBillsPublicDTO);
                        log.info("{}-核心数据转化完成,账号为{}",batch,allBillsPublicDTO.getAcctNo());
                        log.info("{}-核心数据保存开始,账号为{}",batch,allBillsPublicDTO.getAcctNo());

                        //增加重复的CustomerNo保存的问题
                        if(StringUtils.isNotBlank(allBillsPublicDTO.getCustomerNo())){
                            Boolean aBoolean = AmsCoreAccountServiceImpl.duplicateCustomerNoMaps.putIfAbsent(allBillsPublicDTO.getCustomerNo(), false);
                            if(aBoolean == null){//未保存
                                log.info("重复的customerNo-{}的Map中无对应的值，已新增",allBillsPublicDTO.getCustomerNo());
                            }else{//已经在保存的途中
                                log.info("重复的customerNo-{}的Map中有对应的值：{}",allBillsPublicDTO.getCustomerNo(),aBoolean);
                                    for(int i=1;i<50;i++){
                                        Thread.sleep(1000);//停顿1s
                                        log.info("重复的customerNo-{}的Map中有对应的值，重复的次数：{}",allBillsPublicDTO.getCustomerNo(),i);
                                        if(AmsCoreAccountServiceImpl.duplicateCustomerNoMaps.putIfAbsent(allBillsPublicDTO.getCustomerNo(), false) == null){
                                            break;
                                        }
                                    }
                            }
                        }
                        transaction = transactionManager.getTransaction(definition);
                        if (StringUtils.isBlank(allBillsPublicDTO.getBillNo())) {
                            String billNo = billNoSeqService.getBillNo(DateUtils.DateToStr(new Date(), "yyyyMMdd"), allBillsPublicDTO.getOrganCode(), BillTypeNo.valueOf(allBillsPublicDTO.getBillType().name()));
                            // 单据编号
                            allBillsPublicDTO.setBillNo(billNo);
                        }
                        allBillsPublicDTO.setString003("1");
                        allBillsPublicDTO.setString004("0");
                        allBillsPublicDTO.setString005("0");
                        amsCoreAccountService.checkIsNull(allBillsPublicDTO,userDto);
                        transactionManager.commit(transaction);
                        log.debug("{}-核心数据保存完成,账号为{}",batch,allBillsPublicDTO.getAcctNo());
                    }catch(Exception e){
                        log.error(batch+"-核心数据转化或保存报错", e);
                        log.error("{}-保存失败，进行回滚,账号{}",batch,allBillsPublicDTO.getAcctNo());
                        if(!transaction.isCompleted()){
                            try{
                                transactionManager.rollback(transaction);
                            }catch (Exception e1){
                                log.error("{}-回滚失败,账号{}",batch,allBillsPublicDTO.getAcctNo());
                            }
                        }
                        if(coreId != null){
                            String message = e.getMessage();
                            if(org.apache.commons.lang.StringUtils.isNotBlank(message)){
                                AmsCoreAccountServiceImpl.coreErrorReasonMaps.putIfAbsent(coreId,StringUtil.charAtByIndex(message,1000));
                            }else{
                                AmsCoreAccountServiceImpl.coreErrorReasonMaps.putIfAbsent(coreId,"核心数据拆分为客账户时异常");
                            }
                        }
                    }finally {
                        index++;
                        AmsCoreAccountServiceImpl.duplicateCustomerNoMaps.remove(allBillsPublicDTO.getCustomerNo());
                    }
        			if(index >0 && index % 1000==0){
                        log.info("{}-核心数据已经处理{}的数量",batch,index);
                    }
                }
                Long endTime = System.currentTimeMillis();
                log.info("{}-核心数据转化完成,耗时{}秒",batch,(endTime - startTime) / 1000);
            } catch (Exception e) {
                log.error(batch+"-核心数据处理失败", e);
            }
        }
        return System.currentTimeMillis();
    }

    private void saveFinishAccount(CorePublicAccountFinish corePublicAccountFinish){
        corePublicAccountFinishDao.save(corePublicAccountFinish);
    }
}
