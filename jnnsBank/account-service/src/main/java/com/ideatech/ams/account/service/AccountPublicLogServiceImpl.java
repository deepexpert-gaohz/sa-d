package com.ideatech.ams.account.service;

import com.ideatech.ams.account.dao.AccountPublicDao;
import com.ideatech.ams.account.dao.AccountPublicLogDao;
import com.ideatech.ams.account.dto.AccountPublicInfo;
import com.ideatech.ams.account.dto.AccountPublicLogInfo;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.dto.poi.OperatorIdcardDuePoi;
import com.ideatech.ams.account.entity.AccountPublic;
import com.ideatech.ams.account.entity.AccountPublicLog;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.service.poi.OperatorIdcardDueExport;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.util.BeanCopierUtils;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author vantoo
 * @date 17:48 2018/5/28
 */
@Service
@Transactional
@Slf4j
public class AccountPublicLogServiceImpl implements AccountPublicLogService {

    @Autowired
    private AccountPublicLogDao accountPublicLogDao;
    @Autowired
    private AccountPublicDao accountPublicDao;

    @Autowired
    private AccountBillsAllService accountBillsAllService;
    @Autowired
    private ConfigService configService;

    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private AccountsAllService accountsAllService;

    @PersistenceContext
    private EntityManager entityManager; //注入EntityManager

    @Value("${ams.notice.noticeFlag:true}")
    private Boolean noticeFlag;

    @Override
    public AccountPublicLogInfo getOne(Long id) {
        return po2dto(accountPublicLogDao.findOne(id));
    }

    @Override
    public List<AccountPublicLogInfo> findByAccountId(Long accountId) {
        return ConverterService.convertToList(accountPublicLogDao.findByAccountId(accountId),AccountPublicLogInfo.class);
    }

    @Override
    public AccountPublicLogInfo findByRefBillId(Long refBillId) {
        List<AccountPublicLog> logInfoList = accountPublicLogDao.findByRefBillId(refBillId);
        if (CollectionUtils.isNotEmpty(logInfoList)) {
            return ConverterService.convert(logInfoList.get(0), AccountPublicLogInfo.class);
        }
        return null;
    }

    @Override
    public AccountPublicLogInfo findByRefBillIdLast(Long refBillId) {
        List<AccountPublicLog> logInfoList = accountPublicLogDao.findByRefBillIdOrderBySequenceDesc(refBillId);
        if (CollectionUtils.isNotEmpty(logInfoList)) {
            return ConverterService.convert(logInfoList.get(0), AccountPublicLogInfo.class);
        }
        return null;
    }


    @Override
    public AccountPublicLogInfo findByAccountIdBackup(Long accountId) {
        List<AccountPublicLog> accountPublicLogList = accountPublicLogDao.findByAccountId(accountId);
        AccountPublicLogInfo accountPublicLogInfo = null;
        if(CollectionUtils.isNotEmpty(accountPublicLogList)){
            AccountPublicLog accountPublicLog = accountPublicLogList.get(0);
            if(accountPublicLog !=null) {
                accountPublicLogInfo = new AccountPublicLogInfo();
                BeanUtils.copyProperties(accountPublicLog, accountPublicLogInfo);
            }
        }
        return accountPublicLogInfo;
    }

    @Override
    public void save(AccountPublicLogInfo accountPublicLogInfo) {
        AccountPublicLog accountPublicLog = new AccountPublicLog();
        BeanUtils.copyProperties(accountPublicLogInfo, accountPublicLog,new String[]{"id"});
        accountPublicLogDao.save(accountPublicLog);
    }

    @Override
    public void saveOrUpdate(AccountPublicLogInfo accountPublicLogInfo) {
        AccountPublicLog accountPublicLog = null;
        if(accountPublicLogInfo.getId() !=null){
            accountPublicLog = accountPublicLogDao.findOne(accountPublicLogInfo.getId());
        }
        if(accountPublicLog == null){
            accountPublicLog = new AccountPublicLog();
        }
        BeanUtils.copyProperties(accountPublicLogInfo, accountPublicLog);
        accountPublicLogDao.save(accountPublicLog);
    }

    @Override
    public AccountPublicLogInfo getMaxSeq(Long accountId) {
        return po2dto(accountPublicLogDao.findByAccountIdAndMaxSequence(accountId));
    }

    @Override
    public int deleteByAccountIdAndRefBillId(Long accountId, Long refBillId) {
        int count = accountPublicLogDao.deleteByAccountIdAndRefBillId(accountId, refBillId);
        return count;
    }

    @Override
    public void delete(Long id) {
        accountPublicLogDao.delete(id);
    }

    private AccountPublicLogInfo po2dto(AccountPublicLog accountPublicLog) {
        if (accountPublicLog != null) {
            AccountPublicLogInfo info = new AccountPublicLogInfo();
            BeanCopierUtils.copyProperties(accountPublicLog, info);
            return info;
        } else {
            return null;
        }
    }

    @Override
    public List<AccountPublicLogInfo> findByOrganFullIdLike(String organFullId) {
        return ConverterService.convertToList(accountPublicLogDao.findByOrganFullIdLike(organFullId),AccountPublicLogInfo.class);
    }

    @Override
    public List<AccountPublicLogInfo> findByCustomerNo(String customerNo) {
        return ConverterService.convertToList(accountPublicLogDao.findByCustomerNo(customerNo), AccountPublicLogInfo.class);
    }

    /**
     * 通知提醒经办人到期日统计
     * @param afterDateStr
     * @param beforeDateStr
     * @param organFullId
     * @param operatorOverConfigEnabled
     * @return
     */
    @Override
    public Long countOperatorIdcardDueBefore(final String afterDateStr, final String beforeDateStr, final String organFullId, final Boolean operatorOverConfigEnabled) {
        final String nowDateStr = DateUtils.DateToStr(new Date(), "");

        String sql = "select count(*) from AccountPublic t1 , AccountsAll t2 where t1.accountId = t2.id and t1.organFullId like ?1 ";
        //过期逻辑判断
        if (!operatorOverConfigEnabled) {
            sql += " and t1.operatorIdcardDue between ?2 and ?3";
            sql += " and t1.isOperatorIdcardDue = false";
        }else {
            //过期逻辑判断 && 超期逻辑判断
            sql += "and (t1.operatorIdcardDue between ?2 and ?3 or t1.operatorIdcardDue between ?4 and ?5)";
        }
        //是否过滤销户久悬账户
        if (noticeFlag) {
            sql += " and t2.accountStatus = ?6 ";
        }

        Query query = entityManager.createQuery(sql);

        query.setParameter(1, SecurityUtils.getCurrentOrgFullId() + "%");
        query.setParameter(2, nowDateStr);
        query.setParameter(3, afterDateStr);

        if (operatorOverConfigEnabled) {
            query.setParameter(4, "1900-01-01");
            query.setParameter(5, beforeDateStr);
        }
        if (noticeFlag) {
            query.setParameter(6, AccountStatus.normal);
        }

        return (Long) query.getSingleResult();
    }

    /**
     * 通知提醒经办人到期分页
     * @param operatorIdcardDue
     * @param afterDateStr
     * @param beforeDateStr
     * @param organFullId
     * @param pagingDto
     * @return
     */
    @Override
    public PagingDto<AccountPublicLogInfo> listOperatorIdcardDueBefore(final Boolean isOperatorIdcardDue, final String operatorIdcardDue, final String afterDateStr, final String beforeDateStr,
                                                                    final String organFullId, PagingDto pagingDto) throws Exception {
        final String nowDateStr = DateUtils.DateToStr(new Date(), "");
        Pageable pageable = new PageRequest(Math.max(pagingDto.getOffset(), 0), pagingDto.getLimit());

        String sql = "select t1 from AccountPublic t1, AccountsAll t2 where t1.accountId = t2.id and t1.organFullId like ?1 ";
        String sqlCount = "select count(*) from AccountPublic t1 , AccountsAll t2 where t1.accountId = t2.id and t1.organFullId like ?1 ";

        ConfigDto configDto = configService.findOneByConfigKey("operatorOverConfigEnabled");

        if (configDto!=null && Boolean.valueOf(configDto.getConfigValue())) {
            //过期逻辑判断 && 超期逻辑判断
            sql += "and (t1.operatorIdcardDue between ?2 and ?3 or t1.operatorIdcardDue between ?4 and ?5)";
            sqlCount += "and (t1.operatorIdcardDue between ?2 and ?3 or t1.operatorIdcardDue between ?4 and ?5)";
        }else {
            //过期逻辑判断
            sql += " and t1.operatorIdcardDue between ?2 and ?3";
            sqlCount += " and t1.operatorIdcardDue between ?2 and ?3";
            sql += " and t1.isOperatorIdcardDue = false";
            sqlCount += " and t1.isOperatorIdcardDue = false";
        }
        //是否过滤销户久悬账户
        if (noticeFlag) {
            sql += " and t2.accountStatus = ?6 ";
            sqlCount += " and t2.accountStatus = ?6 ";
        }

        Query query = entityManager.createQuery(sql);
        Query queryCount = entityManager.createQuery(sqlCount);

        query.setParameter(1, SecurityUtils.getCurrentOrgFullId() + "%");
        queryCount.setParameter(1, SecurityUtils.getCurrentOrgFullId() + "%");
        query.setParameter(2, nowDateStr);
        queryCount.setParameter(2, nowDateStr);
        query.setParameter(3, afterDateStr);
        queryCount.setParameter(3, afterDateStr);

        if (configDto!=null && Boolean.valueOf(configDto.getConfigValue())) {
            query.setParameter(4, "1900-01-01");
            queryCount.setParameter(4, "1900-01-01");
            query.setParameter(5, beforeDateStr);
            queryCount.setParameter(5, beforeDateStr);
        }
        if (noticeFlag) {
            query.setParameter(6, AccountStatus.normal);
            queryCount.setParameter(6, AccountStatus.normal);
        }

        query.setFirstResult(pagingDto.getOffset() * pagingDto.getLimit());
        queryCount.setFirstResult(pagingDto.getOffset() * pagingDto.getLimit());
        query.setMaxResults(pagingDto.getLimit());
        queryCount.setMaxResults(pagingDto.getLimit());

        //查询
        List<AccountPublic> list = query.getResultList();
        Long count = (Long) queryCount.getSingleResult();

        List<AccountPublicLogInfo> dtoList = new ArrayList<>();

        for(AccountPublic info : list) {
            AccountPublicLogInfo accountPublicLogInfo = new AccountPublicLogInfo();
            BeanCopierUtils.copyProperties(info, accountPublicLogInfo);
            AccountsAllInfo accountsAllInfo = accountsAllService.getOne(info.getAccountId());
            BeanCopierUtils.copyProperties(accountsAllInfo, accountPublicLogInfo);
            AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.getOne(accountsAllInfo.getRefBillId());
            accountPublicLogInfo.setString001(accountBillsAllInfo.getBillType().name());
            accountPublicLogInfo.setString002(accountBillsAllInfo.getDepositorName());
            if(StringUtils.isNotBlank(info.getOperatorIdcardType())) {
                String operatorIdcardTypeName = dictionaryService.transalte("legalIdcardTypeValue2Item", info.getOperatorIdcardType());
                info.setOperatorIdcardType(operatorIdcardTypeName);
            }
            if(info.getCheckStatus()==null || info.getCheckStatus().equals("")){
                info.setCheckStatus("未核实");
            }else{
                info.setCheckStatus("已核实");
            }
            dtoList.add(accountPublicLogInfo);
        }

        pagingDto.setList(dtoList);
        pagingDto.setTotalRecord(count);
        pagingDto.setTotalPages((int) Math.ceil(count.intValue()/pagingDto.getLimit()));
        return pagingDto;
    }

    @Override
    public PagingDto<AccountPublicLogInfo> listOperatorIdcardDueBefore1(final Boolean isOperatorIdcardDue, final String operatorIdcardDue, final String afterDateStr, final String beforeDateStr,
                                                                       final String organFullId, PagingDto pagingDto) throws Exception {
        final String nowDateStr = DateUtils.DateToStr(new Date(), "");
        Pageable pageable = new PageRequest(Math.max(pagingDto.getOffset(), 0), pagingDto.getLimit());
        String sql = "select t1 from AccountPublic t1, AccountsAll t2 where t1.accountId = t2.id and t1.organFullId like ?1 ";
        String sqlCount = "select count(*) from AccountPublic t1 , AccountsAll t2 where t1.accountId = t2.id and t1.organFullId like ?1 ";
        ConfigDto configDto = configService.findOneByConfigKey("operatorOverConfigEnabled");
        if (configDto!=null && Boolean.valueOf(configDto.getConfigValue())) {
            sql += "and (t1.operatorIdcardDue between ?2 and ?3 or t1.operatorIdcardDue between ?4 and ?5)";
            sqlCount += "and (t1.operatorIdcardDue between ?2 and ?3 or t1.operatorIdcardDue between ?4 and ?5)";
        }else {
            sql += " and t1.operatorIdcardDue between ?2 and ?3";
            sqlCount += " and t1.operatorIdcardDue between ?2 and ?3";
            sql += " and t1.isOperatorIdcardDue = false";
            sqlCount += " and t1.isOperatorIdcardDue = false";
        }
        if (noticeFlag) {
            sql += " and t2.accountStatus = ?6 ";
            sqlCount += " and t2.accountStatus = ?6 ";
        }
        Query query = entityManager.createQuery(sql);
        Query queryCount = entityManager.createQuery(sqlCount);
        query.setParameter(1, SecurityUtils.getCurrentOrgFullId() + "%");
        queryCount.setParameter(1, SecurityUtils.getCurrentOrgFullId() + "%");
        query.setParameter(2, nowDateStr);
        queryCount.setParameter(2, nowDateStr);
        query.setParameter(3, afterDateStr);
        queryCount.setParameter(3, afterDateStr);
        if (configDto!=null && Boolean.valueOf(configDto.getConfigValue())) {
            query.setParameter(4, "1900-01-01");
            queryCount.setParameter(4, "1900-01-01");
            query.setParameter(5, beforeDateStr);
            queryCount.setParameter(5, beforeDateStr);
        }
        if (noticeFlag) {
            query.setParameter(6, AccountStatus.normal);
            queryCount.setParameter(6, AccountStatus.normal);
        }
        query.setFirstResult(pagingDto.getOffset() * pagingDto.getLimit());
        queryCount.setFirstResult(pagingDto.getOffset() * pagingDto.getLimit());
        query.setMaxResults(pagingDto.getLimit());
        queryCount.setMaxResults(pagingDto.getLimit());
        List<AccountPublic> list = query.getResultList();
        Long count = (Long) queryCount.getSingleResult();
        List<AccountPublicLogInfo> dtoList = new ArrayList<>();
        for(AccountPublic info : list) {
            AccountPublicLogInfo accountPublicLogInfo = new AccountPublicLogInfo();
            BeanCopierUtils.copyProperties(info, accountPublicLogInfo);
            AccountsAllInfo accountsAllInfo = accountsAllService.getOne(info.getAccountId());
            BeanCopierUtils.copyProperties(accountsAllInfo, accountPublicLogInfo);
            AccountBillsAllInfo accountBillsAllInfo = accountBillsAllService.getOne(accountsAllInfo.getRefBillId());
            accountPublicLogInfo.setString001(accountBillsAllInfo.getBillType().name());
            accountPublicLogInfo.setString002(accountBillsAllInfo.getDepositorName());
            if(StringUtils.isNotBlank(info.getOperatorIdcardType())) {
                String operatorIdcardTypeName = dictionaryService.transalte("legalIdcardTypeValue2Item", info.getOperatorIdcardType());
                info.setOperatorIdcardType(operatorIdcardTypeName);
            }
            if(info.getCheckStatus()==null || info.getCheckStatus().equals("")){
                info.setCheckStatus("未核实");
            }else{
                info.setCheckStatus("已核实");
            }
            dtoList.add(accountPublicLogInfo);
        }
        pagingDto.setList(dtoList);
        pagingDto.setTotalRecord(count);
        pagingDto.setTotalPages((int) Math.ceil(count.intValue()/pagingDto.getLimit()));
        return pagingDto;
    }
    @Override
    public IExcelExport exportOperatorIdcardDueBefore(final Boolean isOperatorIdcardDue,final String operatorIdcardDue,final String afterDate,final String beforeDate,final String organFullId) throws Exception {
        final String nowDateStr = DateUtils.DateToStr(new Date(), "");

        String sql = "select t1 from AccountPublic t1, AccountsAll t2 where t1.accountId = t2.id and t1.organFullId like ?1 ";

        ConfigDto configDto = configService.findOneByConfigKey("operatorOverConfigEnabled");

        if (configDto!=null && Boolean.valueOf(configDto.getConfigValue())) {
            //过期逻辑判断 && 超期逻辑判断
            sql += "and (t1.operatorIdcardDue between ?2 and ?3 or t1.operatorIdcardDue between ?4 and ?5)";
        }else {
            //过期逻辑判断
            sql += " and t1.operatorIdcardDue between ?2 and ?3";
            sql += " and t1.isOperatorIdcardDue = false";
        }
        //是否过滤销户久悬账户
        if (noticeFlag) {
            sql += " and t2.accountStatus = ?6 ";
        }
        if (isOperatorIdcardDue!=null){
            sql += " and t1.isOperatorIdcardDue = ?7 ";
        }
        if (operatorIdcardDue!=null){
            sql += " and t1.operatorIdcardDue = ?8 ";
        }

        Query query = entityManager.createQuery(sql);

        query.setParameter(1, SecurityUtils.getCurrentOrgFullId() + "%");
        query.setParameter(2, nowDateStr);
        query.setParameter(3, afterDate);

        if (configDto!=null && Boolean.valueOf(configDto.getConfigValue())) {
            query.setParameter(4, "1900-01-01");
            query.setParameter(5, beforeDate);
        }
        if (noticeFlag) {
            query.setParameter(6, AccountStatus.normal);
        }
        if (isOperatorIdcardDue!=null){
            query.setParameter(7,isOperatorIdcardDue);
        }
        if (operatorIdcardDue!=null){
            query.setParameter(8,operatorIdcardDue);
        }

        List<AccountPublic> accountPublicLogList = query.getResultList();

        //2、导出
        IExcelExport iExcelExport = new OperatorIdcardDueExport();
        List<OperatorIdcardDuePoi> fileDuePoiList = new ArrayList<>();

        for (AccountPublic accountPublicLog :accountPublicLogList){
            OperatorIdcardDuePoi operatorIdcardDuePoi = new OperatorIdcardDuePoi();
            ConverterService.convert(accountPublicLog,operatorIdcardDuePoi);
            AccountsAllInfo accountsAllInfo = accountsAllService.getOne(accountPublicLog.getAccountId());
            BeanCopierUtils.copyProperties(accountsAllInfo, operatorIdcardDuePoi);

            operatorIdcardDuePoi.setOperatorIdcardType(dictionaryService.transalte("legalTypeValue2Item",accountPublicLog.getOperatorIdcardType()));

            if (null!=accountPublicLog.getIsOperatorIdcardDue()&&accountPublicLog.getIsOperatorIdcardDue()){
                operatorIdcardDuePoi.setIsOperatorIdcardDue("是");
            }else {
                operatorIdcardDuePoi.setIsOperatorIdcardDue("否");
            }
            fileDuePoiList.add(operatorIdcardDuePoi);
        }

        iExcelExport.setPoiList(fileDuePoiList);

        return iExcelExport;
    }

    @Override
    public List<AccountPublicInfo> getOperatorDueAndOver(final String afterDate, final String beforeDate, final Boolean operatorOverConfigEnabled) {

        String sql = "select t1 from AccountPublic t1, AccountsAll t2 where t1.accountId = t2.id ";

        ConfigDto configDto = configService.findOneByConfigKey("operatorOverConfigEnabled");

        if (configDto!=null && Boolean.valueOf(configDto.getConfigValue())) {
            //过期逻辑判断 && 超期逻辑判断
            sql += "and (t1.operatorIdcardDue = ?1 or t1.operatorIdcardDue = ?2)";
        }else {
            //过期逻辑判断
            sql += " and t1.operatorIdcardDue = ?1";
        }
        //是否过滤销户久悬账户
        if (noticeFlag) {
            sql += " and t2.accountStatus = ?3 ";
        }

        Query query = entityManager.createQuery(sql);

        query.setParameter(1, afterDate);

        if (configDto!=null && Boolean.valueOf(configDto.getConfigValue())) {
            query.setParameter(2, beforeDate);
        }
        if (noticeFlag) {
            query.setParameter(3, AccountStatus.normal);
        }

        List<AccountPublic> accountPublicList = query.getResultList();
        return ConverterService.convertToList(accountPublicList,AccountPublicInfo.class);
    }


}
